package uz.smartarena.edu.iu.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.data.remote.FirebaseRemote
import uz.smartarena.edu.databinding.FragmentHomeBinding
import uz.smartarena.edu.iu.adapter.ThemeAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = ThemeAdapter()
    private val remote = FirebaseRemote.getInstance()
    private val storage = EncryptedLocalStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        remote.getAcceptanceByTheme(storage.uid,"7-sinf", "1"){c, b ->
            Log.d("TTT", "onCreateView: $c $b")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setListener {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        adapter.submitList(listOf("Sonlar", "Ayirish", "Qo'shish", "Ko'paytirish", "Bo'lish"))

        binding.themesList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}