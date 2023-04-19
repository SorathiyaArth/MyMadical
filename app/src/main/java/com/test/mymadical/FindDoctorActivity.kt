package com.test.mymadical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.mymadical.Adepter.AdepterFindDoctor
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class FindDoctorActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var toolbar: Toolbar? = null
    lateinit var tvtitle: TextView
    lateinit var imgsearch: ImageView
    lateinit var tvcart: RelativeLayout
    lateinit var Rv_Doctor: RecyclerView
    lateinit var Sp_Dis: Spinner
    var cityname: String? = null
    var City: Array<String> = arrayOf()
    val listdoctor: ArrayList<String> = ArrayList()
    val listimage: ArrayList<Int> = ArrayList()
    var adepterfinddoctor: AdepterFindDoctor? = null


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finddoctor)


        arreylistdata()

        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        tvcart = findViewById(R.id.tvcart)
        toolbar = findViewById(R.id.toolbar)
        Rv_Doctor = findViewById(R.id.Rv_Doctor)
        Sp_Dis = findViewById(R.id.Sp_Dis)
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        tvtitle.setText("Find My Doctor")
        imgsearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        tvcart.setOnClickListener {
            if(Utils().islogin(this)){
                val intent = Intent(this, ActivityMyCart::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }


        Sp_Dis.onItemSelectedListener = this

        val ad: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, City)
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Sp_Dis.adapter = ad

        getdoctortypes()


    }

    private fun getdoctortypes() {
        Rv_Doctor.layoutManager = GridLayoutManager(this, 3)
        adepterfinddoctor = AdepterFindDoctor(listdoctor, listimage, this)
        Rv_Doctor.adapter = adepterfinddoctor

        adepterfinddoctor?.setOnItemClickListener(object : ClickInterface {
            override fun onclicked12(position: Int, Types: Int) {
                if (Types == 1) {

                        val intent = Intent(this@FindDoctorActivity, ListDoctor::class.java)
                        intent.putExtra("city_name",cityname)
                        intent.putExtra("doc_type",listdoctor[position])
                        intent.putExtra("doc_img",listimage[position])
                        startActivity(intent)

                }
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (City.get(position) != "--Select Your City--") {


            val mylf = this.layoutInflater
            val myview: View = mylf.inflate(R.layout.toast_layout, null)
            val text_data = myview.findViewById<TextView>(R.id.toast_text)
            text_data.text = "Search for " + City.get(position)
            val mytoast = Toast(this)
            mytoast.duration = Toast.LENGTH_SHORT
            mytoast.setView(myview)
            // mytoast.setGravity(Gravity.NO_GRAVITY, 0, 0)
            if (!text_data.text.toString().isEmpty()) {
                mytoast.show()
            }

            cityname = City.get(position)


        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Please Select Your City", Toast.LENGTH_SHORT).show()

    }

    private fun arreylistdata() {
        listdoctor.add("Allergists/Immunologists")
        listimage.add(R.drawable.allergist)
        listdoctor.add("Anesthesiologists")
        listimage.add(R.drawable.anesthesiologist)
        listdoctor.add("Cardiologists")
        listimage.add(R.drawable.cardiologist)
        listdoctor.add("Colon and Rectal Surgeons")
        listimage.add(R.drawable.rectalsurgeons)
        listdoctor.add("Critical Care Medicine Specialists")
        listimage.add(R.drawable.criticalcare)
        listdoctor.add("Dermatologists")
        listimage.add(R.drawable.dermatologist)
        listdoctor.add("Endocrinologists")
        listimage.add(R.drawable.endocrinology)
        listdoctor.add("Emergency Medicine Specialists")
        listimage.add(R.drawable.medical)
        listdoctor.add("Family Physicians")
        listimage.add(R.drawable.family)
        listdoctor.add("Gastroenterologists")
        listimage.add(R.drawable.gastroenterologist)
        listdoctor.add("Geriatric Medicine Specialists")
        listimage.add(R.drawable.nursing)
        listdoctor.add("Hematologists")
        listimage.add(R.drawable.hematologist)
        listdoctor.add("Hospice and Palliative Medicine Specialists")
        listimage.add(R.drawable.palliativemedicine)
        listdoctor.add("Infectious Disease Specialists")
        listimage.add(R.drawable.infectiousdisease)
        listdoctor.add("Internists")
        listimage.add(R.drawable.internist)
        listdoctor.add("Medical Geneticists")
        listimage.add(R.drawable.medicalgeneticists)
        listdoctor.add("Nephrologists")
        listimage.add(R.drawable.kidneys)
        listdoctor.add("Neurologists")
        listimage.add(R.drawable.neurologists)
        listdoctor.add("Obstetricians and Gynecologists")
        listimage.add(R.drawable.obstetriciansandgynecologists)
        listdoctor.add("Oncologists")
        listimage.add(R.drawable.oncologists)
        listdoctor.add("Ophthalmologists")
        listimage.add(R.drawable.ophthalmologist)
        listdoctor.add("Osteopaths")
        listimage.add(R.drawable.osteopaths)
        listdoctor.add("Otolaryngologists")
        listimage.add(R.drawable.otolaryngologist)
        listdoctor.add("Pathologists")
        listimage.add(R.drawable.pathologist)
        listdoctor.add("Pediatricians")
        listimage.add(R.drawable.pediatrician)
        listdoctor.add("Physiatrists")
        listimage.add(R.drawable.physiatrists)
        listdoctor.add("Plastic Surgeons")
        listimage.add(R.drawable.plasticsurgeons)
        listdoctor.add("Podiatrists")
        listimage.add(R.drawable.podiatrist)
        listdoctor.add("Preventive Medicine Specialists")
        listimage.add(R.drawable.prevention)
        listdoctor.add("Psychiatrists")
        listimage.add(R.drawable.psychiatrists)
        listdoctor.add("Pulmonologists")
        listimage.add(R.drawable.pulmonologists)
        listdoctor.add("Radiologists")
        listimage.add(R.drawable.radiologist)
        listdoctor.add("Rheumatologists")
        listimage.add(R.drawable.rheumatologists)
        listdoctor.add("Sports Medicine Specialists")
        listimage.add(R.drawable.sportsmedicinespecialists)
        listdoctor.add("General Surgeons")
        listimage.add(R.drawable.generalsurgeons)
        listdoctor.add("Urologists")
        listimage.add(R.drawable.urology)



        City = arrayOf(

            "--Select Your City--",
            "Ahmedabad",
            "Amreli",
            "Anand",
            "Aravalli",
            "Banaskantha",
            "Bharuch",
            "Bhavnagar",
            "Botad",
            "Chhota Udepur",
            "Dahod",
            "Dangs",
            "Devbhoomi Dwarka",
            "Gandhinagar",
            "Gir Somnath",
            "Jamnagar",
            "Junagadh",
            "Kachchh",
            "Kheda",
            "Mahisagar",
            "Mehsana",
            "Morbi",
            "Narmada",
            "Navsari",
            "Panchmahal",
            "Patan",
            "Porbandar",
            "Rajkot",
            "Sabarkantha",
            "Surat",
            "Surendranagar",
            "Tapi",
            "Vadodara",
            "Valsad"

        )


    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}