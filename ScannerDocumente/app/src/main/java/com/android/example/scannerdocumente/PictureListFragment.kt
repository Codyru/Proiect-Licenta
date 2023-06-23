package com.android.example.scannerdocumente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Files.delete


class PictureListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PictureAdapter
    private lateinit var pictureDao: ImageDataDao

    private val deleteListener = object : PictureAdapter.OnDeleteListener {
        override fun onDelete(entry: ImageData) {
            deleteEntry(entry)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_picture_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PictureAdapter(emptyList(), deleteListener)
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appDataBase = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
        pictureDao = appDataBase.imageDataDao()

        lifecycleScope.launch {
            val entries = withContext(Dispatchers.IO) {
                pictureDao.getAllImages()
            }

            adapter.setData(entries)
        }

    }

    private fun deleteEntry(entry: ImageData) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                pictureDao.delete(entry)
            }
            // Refresh the RecyclerView after deletion
            val updatedEntries = withContext(Dispatchers.IO) {
                pictureDao.getAllImages()
            }
            adapter.setData(updatedEntries)
        }
    }


}