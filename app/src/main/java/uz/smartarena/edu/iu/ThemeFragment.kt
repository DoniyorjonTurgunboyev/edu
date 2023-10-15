package uz.smartarena.edu.iu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.smartarena.edu.data.remote.FirebaseRemote
import uz.smartarena.edu.databinding.FragmentThemesBinding
import uz.smartarena.edu.iu.adapter.ThemeAdapter

class ThemeFragment : Fragment() {
    private var _binding: FragmentThemesBinding? = null
    private val binding get() = _binding!!
    private val adapter = ThemeAdapter()
    private val remote = FirebaseRemote.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remote.getThemesByClassName("7-sinf") {
            adapter.submitList(it)
        }
        binding.themesList.adapter = adapter
        adapter.setListener {
            findNavController().navigate(ThemeFragmentDirections.actionThemeFragmentToLessonFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}