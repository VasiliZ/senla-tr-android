package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drawer.ui.nootebook.EditFileFragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.NotebookLandFragmentBinding

class LandNoteBookFragment : Fragment() {
    private var binding: NotebookLandFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotebookLandFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.listFileContainer, NotebookFragment())
        transaction.addToBackStack(null)
        transaction.replace(R.id.contentContainer, EditFileFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}