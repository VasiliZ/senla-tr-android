package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.ParentFragmentNotebookBinding

class ParentFragmentNotebook : Fragment() {
    private var binding: ParentFragmentNotebookBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ParentFragmentNotebookBinding.inflate(inflater)
        return binding?.root ?: error("can't bind parent fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let {
            val manager = childFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.containerForChild, LandNoteBookFragment())
            transaction.commit()
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}