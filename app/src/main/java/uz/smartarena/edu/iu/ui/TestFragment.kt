package uz.smartarena.edu.iu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.smartarena.edu.R
import uz.smartarena.edu.data.local.TestLists
import uz.smartarena.edu.databinding.FragmentTestBinding

class TestFragment : Fragment() {
    private var current = 1
    private var rightCount = 0
    private var data = TestLists.getTests()
    private var check = false
    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!
    private lateinit var k: ArrayList<TextView>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        k = arrayListOf(binding.a, binding.b, binding.c, binding.d)
        setVars()
        k.forEach { click ->
            click.setOnClickListener {
                if (!check) {
                    check = true
                    click.setTextColor(resources.getColor(R.color.red))
                    if (data.answer == click.text.toString()) {
                        rightCount++
                    }
                    clickWrongAnswer()
                } else {
                    Toast.makeText(requireContext(), "Qayta tanlash mumkin emas", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.enter.setOnClickListener {
            k.forEach {
                it.setTextColor(resources.getColor(R.color.seriy))
            }
            if (check) {
                check = false
                setVars()
                current++
                binding.textView3.setText("$current/10")
                if (current == 11) {
                    Toast.makeText(requireContext(), "Topilganlar : $rightCount", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(requireContext(), "Avval variantni tanlang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun clickWrongAnswer() {
        k.forEach { click ->
            if (data.answer == click.text.toString()) {
                click.setTextColor(resources.getColor(R.color.green))
            }
        }
    }

    fun setVars() {
        data.variants.shuffle()
        for (i in k.indices) {
            k[i].setText(data.variants[i])
        }
    }
}