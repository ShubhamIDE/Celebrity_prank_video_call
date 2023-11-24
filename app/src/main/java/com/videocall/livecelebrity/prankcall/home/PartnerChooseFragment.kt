package com.videocall.livecelebrity.prankcall.home

import android.app.Person
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.videocall.livecelebrity.prankcall.R
import com.videocall.livecelebrity.prankcall.databinding.FragmentPartnerChooseBinding
import com.videocall.livecelebrity.prankcall.databinding.RvLangItemBinding
import com.videocall.livecelebrity.prankcall.databinding.RvPartnerItemBinding
import com.videocall.livecelebrity.prankcall.splash.ItemOffsetDecoration
import com.videocall.livecelebrity.prankcall.splash.LanguageAdapter
import com.videocall.livecelebrity.prankcall.splash.LanguageClickListener

class PartnerChooseFragment : Fragment() {

    private lateinit var binding: FragmentPartnerChooseBinding

    companion object{
        const val TYPE_KEY = "type_key"
        const val TYPE_MESSAGE = "Message"
        const val TYPE_AUDIO = "Audio"
        const val TYPE_VIDEO = "video"
        var selectedType: String = TYPE_MESSAGE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPartnerChooseBinding.inflate(inflater, container, false)

        val partnersList = listOf(
            Pair("Name1", "Profession1"),
            Pair("Name2", "Profession2"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
            Pair("Name1", "Profession1"),
        )

        binding.rvPartners.adapter = ChoosePartnerAdapter(partnersList){
            if(selectedType == TYPE_MESSAGE){
                findNavController().navigate(R.id.action_partnerChooseFragment_to_chatsFragment)
            }
            else if(selectedType == TYPE_AUDIO){
                findNavController().navigate(R.id.action_partnerChooseFragment_to_audioFragment)
            }
            else{
                findNavController().navigate(R.id.action_partnerChooseFragment_to_videoFragment)
            }
        }

        binding.rvPartners.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPartners.addItemDecoration(ItemOffsetDecoration(30))
        return binding.root
    }
}


class ChoosePartnerAdapter(
    private val personsList: List<Pair<String, String>>,
    val onClick: (pos: Int) -> Unit
): RecyclerView.Adapter<ChoosePartnerAdapter.PersonVH>()
{
    inner class PersonVH(val binding: RvPartnerItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH {
        val binding = RvPartnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonVH(binding);
    }

    override fun getItemCount() = personsList.size

    override fun onBindViewHolder(holder: PersonVH, position: Int) {
        Glide.with(holder.binding.ivImg).load(R.drawable.bg_blue).into(holder.binding.ivImg)
        val name = personsList[holder.adapterPosition].first
        val profession = personsList[holder.adapterPosition].second
        holder.binding.tvName.text = name
        holder.binding.tvProfession.text = profession
        holder.itemView.setOnClickListener {
            onClick(holder.adapterPosition)
        }
    }
}
