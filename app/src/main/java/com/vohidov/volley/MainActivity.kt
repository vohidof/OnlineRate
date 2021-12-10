package com.vohidov.volley

import Adapter.ItemClick
import Adapter.RvAdapter
import Model.Soqqa
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vohidov.volley.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_layout.*
import org.json.JSONArray
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var url = "http://cbu.uz/uzc/arkhiv-kursov-valyut/json/"
    lateinit var requestQueue: RequestQueue
    lateinit var rvAdapter: RvAdapter
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        requestQueue = Volley.newRequestQueue(this)

        binding.edtSearch.isVisible = false

        binding.imgToolBar.setOnClickListener {
            if (binding.txtToolBar.isVisible == true && binding.edtSearch.isVisible == false) {
                binding.txtToolBar.isVisible = false
                binding.edtSearch.isVisible = true
            } else {
                binding.txtToolBar.isVisible = true
                binding.edtSearch.isVisible = false
            }

        }



        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            object : Response.Listener<JSONArray> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(response: JSONArray?) {

                    val type = object : TypeToken<List<Soqqa>>() {}.type
                    val list = Gson().fromJson<ArrayList<Soqqa>>(response.toString(), type)


                    fun filter(toString: String) {
                        var filteredList: ArrayList<Soqqa>
                        filteredList = ArrayList()
                        filteredList.sortBy { Soqqa -> Soqqa.CcyNm_UZ }

                        for (item in list) {
                            if (item.CcyNm_UZ.toLowerCase().contains(toString.toLowerCase())) {
                                filteredList.add(item)
                                filteredList.sortBy { Soqqa -> Soqqa.CcyNm_UZ }
                            }
                        }

                        rvAdapter.filterList(filteredList)
                    }

                    binding.edtSearch.addTextChangedListener(object : TextWatcher {

                        override fun afterTextChanged(s: Editable) {
                            filter(s.toString())
                        }

                        override fun beforeTextChanged(
                            s: CharSequence, start: Int,
                            count: Int, after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence, start: Int,
                            before: Int, count: Int
                        ) {
                        }
                    })

                    rvAdapter = RvAdapter(list, object : ItemClick {
                        @SuppressLint("InflateParams")
                        override fun onClick1(list: List<Soqqa>, position: Int) {
                            val bottomSheetDialog = BottomSheetDialog(this@MainActivity)
                            bottomSheetDialog.setContentView(
                                layoutInflater.inflate(
                                    R.layout.dialog_layout,
                                    null,
                                    false
                                )
                            )
                            bottomSheetDialog.txt_rate_name.text = list[position].CcyNm_UZ
                            bottomSheetDialog.txt_rate_value.text = list[position].Rate

                            bottomSheetDialog.show()
                        }
                    })

                    rvAdapter.notifyDataSetChanged()
                    binding.rv.adapter = rvAdapter

                    Log.d(TAG, "onResponse : ${response.toString()}")
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })

        requestQueue.add(jsonArrayRequest)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.txtToolBar.isVisible == false && binding.edtSearch.isVisible == true) {
            binding.txtToolBar.isVisible = true
            binding.edtSearch.isVisible = false
        }
    }
}