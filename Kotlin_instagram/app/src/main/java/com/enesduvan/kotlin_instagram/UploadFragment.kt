package com.enesduvan.kotlin_instagram

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.enesduvan.kotlin_instagram.databinding.FragmentUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.util.UUID

class UploadFragment : Fragment(R.layout.fragment_upload) {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permission_launcher: ActivityResultLauncher <String>
    var image_uri : Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUploadBinding.bind(view)
        binding.imageView5.setOnClickListener { select_image() }
        register_launcher()
        binding.uploadButton4.setOnClickListener { upload() }
        auth = Firebase.auth
        storage = Firebase.storage
        firestore = Firebase.firestore
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun select_image(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                //eğer izin verilmediyse
                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)){
                    //izinin ne için istendiği
                    Snackbar.make(requireView(),"Galeri için izin verilmesi gerekiyor", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Izin ver", View.OnClickListener{
                            //izin isteme
                            permission_launcher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                }else{
                    //request permission //izin isteme
                    Snackbar.make(requireView(),"Galeri için izin verilmesi gerekiyor", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Izin ver", View.OnClickListener{
                            //izin isteme
                            permission_launcher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                }
            }else{
                //izin verildi
                val galery_intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //galeriye gönderme
                activityResultLauncher.launch(galery_intent)
            }
        }else{
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //eğer izin verilmediyse
                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //izinin ne için istendiği
                    Snackbar.make(requireView(),"Galeri için izin verilmesi gerekiyor", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Izin ver", View.OnClickListener{
                            //izin isteme
                            permission_launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                }else{
                    //request permission //izin isteme
                    Snackbar.make(requireView(),"Galeri için izin verilmesi gerekiyor", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Izin ver", View.OnClickListener{
                            //izin isteme
                            permission_launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                }
            }else{
                //izin verildi
                val galery_intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //galeriye gönderme
                activityResultLauncher.launch(galery_intent)
            }
        }
    }

    //DEĞİŞKENLER
    //İZİNİN VERİLECEĞİ BUTON
    //FONKSİYON
    private fun register_launcher(){
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                if (result.resultCode == RESULT_OK){
                    val intent_result = result.data
                    if(intent_result!=null){
                        image_uri = intent_result.data
                        val bitmap = uriToBitmap(image_uri!!)
                        val smallBitmap = small_bitmap(bitmap)
                        binding.imageView5.setImageBitmap(smallBitmap)
                    }
                }
            }

        permission_launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
                if (result){
                    //permission granted
                    val galery_intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    //galeriye gönderme
                    activityResultLauncher.launch(galery_intent)
                }else{
                    //permission denied
                    Toast.makeText(requireActivity(),"izin verilmesi gerekiyor", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun upload(){

        if (image_uri == null){
            Toast.makeText(requireActivity(),"Resim seçilmedi",Toast.LENGTH_SHORT).show()
            return
        }

        binding.uploadProgress.visibility = View.VISIBLE
        binding.uploadButton4.isEnabled = false
        // URI -> Bitmap
        val bitmap = uriToBitmap(image_uri!!)
        // Bitmap küçültme
        val smallBitmap = small_bitmap(bitmap)

        // Bitmap -> ByteArray (sıkıştırma)
        val outputStream = ByteArrayOutputStream()
        smallBitmap.compress(Bitmap.CompressFormat.JPEG,75,outputStream)
        val imageBytes = outputStream.toByteArray()

        val reference = storage.reference
        val uuid = UUID.randomUUID()
        //resim idsi benzersiz yapma
        val image_name = "$uuid.jpg"
        val image_reference = reference.child("images/$image_name")

        //putFile ❌ yerine putBytes ✅
        image_reference.putBytes(imageBytes)
            .addOnSuccessListener {

                //GERÇEK DOWNLOAD URL ALMA
                image_reference.downloadUrl.addOnSuccessListener { uri ->

                    val download_url = uri.toString()

                    val hash_map = hashMapOf<String,Any>()
                    //sözlük yapısı string ->key
                    //              any -> belirsiz veri

                    if(auth.currentUser != null) {
                        //kullanıcı giriş yaptıysa
                        val email = auth.currentUser!!.email!!
                        //aktarım hedefim

                        var name = AppData.kullaniciAdi.toString()
                        if (name == ""){
                            for (item in email){
                                if(item == '@'){
                                    // burdan sonrası artık @gmail.com
                                    break
                                }
                                name += item
                            }
                        }


                        hash_map.put("download_url", download_url)
                        hash_map.put("name", name)
                        hash_map.put("comment", binding.commentText3.text.toString())
                        hash_map.put("date_time", Timestamp.now())

                        //veri tabanuna atma
                        firestore.collection("posts")
                            .add(hash_map)
                            .addOnSuccessListener {
                                Toast.makeText(requireActivity(),"Yüklendi",Toast.LENGTH_SHORT).show()
                                binding.uploadProgress.visibility = View.GONE
                                binding.uploadButton4.isEnabled = true
                                requireActivity().supportFragmentManager.popBackStack()

                            }
                            .addOnFailureListener {
                                Toast.makeText(requireActivity(), it.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        }
    }

    fun small_bitmap(image : Bitmap) : Bitmap{
        var width = image.width
        var height = image.height
        var ratio : Double = width.toDouble()/height.toDouble()

        if (ratio > 1){
            //resim yatay dikdörtgen
            width = 1080
            height = (width / ratio).toInt()
        }else{
            //resim dikey dikdörtgen veya kare
            height = 1080
            width = (height * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }
}
