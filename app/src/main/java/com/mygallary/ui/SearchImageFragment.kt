package com.mygallary.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mygallary.R
import com.mygallary.repository.datamodel.SearchImageResponse
import com.mygallary.repository.remote.ResultWrapper
import com.mygallary.utils.AppUtils
import com.mygallary.utils.LocalLog
import com.mygallary.utils.PaginationScrollListener
import com.mygallary.utils.SpacesItemDecoration
import com.mygallary.viewmodel.SearchImageViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_search_image.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchImageFragment : Fragment() {

    val TAG = SearchImageFragment::class.java.name

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchImageViewModel by viewModels { viewModelFactory }

    lateinit var adapter: SearchImagesRecyclerAdapter

    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        LocalLog.d(TAG,"onCreate")
    }

    override fun onAttach(context: Context) {
        LocalLog.d(TAG,"onAttach")
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LocalLog.d(TAG,"onActivityCreated")
        activity?.title = "Home"
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(false)

    }

    override fun onDetach() {
        super.onDetach()
        LocalLog.d(TAG,"onDetach")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? { return inflater.inflate(R.layout.fragment_search_image, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocalLog.d(TAG,"onViewCreated")

        listenLiveDataChange()

        setUpAdapter()

        recyclerView.autoFitColumns(150)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
        addScrollListener()

        textInputSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH){

                    if (p0?.text.isNullOrBlank()){

                        Toast.makeText(activity,"Search field should't be blank",Toast.LENGTH_SHORT).show()

                    }else {

                        viewModel.list.clear()

                        viewModel.query = p0?.text.toString()

                        viewModel.query?.let { viewModel.searchImages(it)}

                        activity?.let { AppUtils.hideSoftKeyboard(it as AppCompatActivity) }

                    }

                    return true

                }
                return false
            }
        })

        textInputSearch.addTextChangedListener(textChangeListener)

        buttonRetry.setOnClickListener { viewModel.query?.let { viewModel.searchImages(query = it) }}

    }

    private val textChangeListener : TextWatcher = object : TextWatcher {

        private var searchQuery : String = ""

        override fun afterTextChanged(p0: Editable?) {
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            val newSearchQuery : String = p0.toString().trim()

            LocalLog.d(TAG," onTextChanged newSearchQuery = $newSearchQuery")

            if (newSearchQuery == searchQuery)
                return

            searchQuery = newSearchQuery
            LocalLog.d(TAG," onTextChanged searchQuery = $searchQuery")

            // use Main thread for debounce and search logic
            CoroutineScope(Dispatchers.Main).launch {

                // debounce time
                delay(250)

                if (searchQuery != newSearchQuery)
                    return@launch

                // search image
                LocalLog.d(TAG," onTextChanged final searchQuery = $searchQuery")
                viewModel.list.clear()
                viewModel.query = searchQuery
                viewModel.searchImages(searchQuery)

            }
        }
    }

    private fun setUpAdapter(){

        adapter = SearchImagesRecyclerAdapter(viewModel.list, onItemClick = {

            Log.d(TAG,"id = "+it)

            if (!it.images.isNullOrEmpty()) {
                val title = it.title
                val link = it.images[0].link
                val id = it.id

                val bundle = bundleOf("link" to link, "title" to title, "id" to id)
                view?.findNavController()?.navigate(R.id.imagePreviewFragment, bundle)
            }
        })

        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        recyclerView.adapter = adapter

    }

    private fun addScrollListener(){

        recyclerView.addOnScrollListener(object : PaginationScrollListener(recyclerView.layoutManager as GridLayoutManager) {

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun loadMoreItems() {
                isLoading = true;
                loadNextPage()

            }

        })


    }

    private fun listenLiveDataChange() {

        viewModel.imageList.observe(viewLifecycleOwner, Observer {

            when (it) {

                is ResultWrapper.Success -> {

                    LocalLog.d(TAG, "onChange " + it.value.toString())


                  //  viewModel.list.clear()
                    viewModel.list.addAll(it.value.data)

                    adapter.notifyDataSetChanged()

                    progressBarSearch.visibility = View.INVISIBLE
                    errorLayout.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE

                    if (it.value.data.isNotEmpty())
                        adapter.addLoadingFooter()
                    else
                        isLastPage = true


                }
                is ResultWrapper.Error -> {

                    LocalLog.d(TAG, "onChange serachImage Exception " + it.message)

                    progressBarSearch.visibility = View.INVISIBLE
                    textViewError.text = it.message
                    errorLayout.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE


                }

                is ResultWrapper.Loading -> {

                    LocalLog.d(TAG, "onChange serachImage Loading " + it.boolean)

                    progressBarSearch.visibility = View.VISIBLE
                    errorLayout.visibility = View.INVISIBLE
                }

            }
        })

    }

    private fun loadNextPage(){

        LocalLog.d(TAG,"loadNextPage ${viewModel.currentPage+1} ")
        LocalLog.d(TAG,"loadNextPage ${viewModel.query}")

        viewModel.query?.let { viewModel.loadMore(it, viewModel.currentPage+1).observe(viewLifecycleOwner,
            Observer {

                when (it) {

                    is ResultWrapper.Success -> {

                        LocalLog.d(TAG, "onChange serachImage" + it.value.toString())

                        Handler().postDelayed({

                            adapter.removeLoadingFooter()
                            isLoading = false

                            viewModel.currentPage = viewModel.currentPage +1

                            adapter.addAll(it.value.data)

                            if (it.value.data.isNotEmpty())
                                adapter.addLoadingFooter()
                            else
                                isLastPage = true

                        },500)

                    }
                    is ResultWrapper.Error -> {

                        LocalLog.d(TAG, "onChange searchImage Exception " + it.message)

                    }

                    is ResultWrapper.Loading -> {

                        LocalLog.d(TAG, "onChange serachImage Loading " + it.boolean)

                    }

                }

            })
        }
    }

    /**
     * @param columnWidth - in dp
     */
    fun RecyclerView.autoFitColumns(columnWidth: Int) {
        val displayMetrics = this.context.resources.displayMetrics
        val noOfColumns = ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
        this.layoutManager = GridLayoutManager(this.context, noOfColumns)
    }

}