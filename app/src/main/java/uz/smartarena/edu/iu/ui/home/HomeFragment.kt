package uz.smartarena.edu.iu.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.data.local.SubjectList
import uz.smartarena.edu.data.remote.FirebaseRemote
import uz.smartarena.edu.databinding.FragmentHomeBinding
import uz.smartarena.edu.iu.adapter.SubjectAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = SubjectAdapter()
    private val remote = FirebaseRemote.getInstance()
    private val storage = EncryptedLocalStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        remote.getAcceptanceByTheme(storage.uid, "7-sinf", "1") { c, b ->
            Log.d("TTT", "onCreateView: $c $b")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToThemeFragment())
        }
        binding.bookList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.submitList(SubjectList.getBookList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}