package com.mygallary.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mygallary.R;
import com.mygallary.repository.local.Image;
import com.mygallary.utils.LocalLog;
import com.mygallary.viewmodel.ImagePreviewViewModel;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class ImagePreviewFragment extends Fragment {

    public ImagePreviewFragment() {
        // Required empty public constructor
    }

     private static final String TAG  = "ImagePreviewFragment";

    final static String ARG_LINK = "link";
    final static String ARG_ID = "id";
    final static String ARG_TITLE = "title";


    String id;
    String link;
    String title;

    ImageView imageView;
    LinearLayout addCommentLayout;
    LinearLayout viewCommentLayout;
    TextView textViewComment;
    Button btSubmitComment;
    EditText editTextComment;

    ImagePreviewViewModel viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this,viewModelFactory).get(ImagePreviewViewModel.class);

        if (getArguments() !=null) {
            id = getArguments().getString(ARG_ID);
            link = getArguments().getString(ARG_LINK);
            title = getArguments().getString(ARG_TITLE);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpUI(view);

        loadImage(link);


         /*
          fetch image comment form local database,
          if return empty - show add comment view
          else - show comment
          */
        viewModel.getImageComment(id).observe(getViewLifecycleOwner(),new Observer<Image>() {
             @Override
             public void onChanged(Image image) {

                 LocalLog.INSTANCE.d(TAG,"onChanged "+image);

                 if (image == null){

                     showAddCommentView();

                 }else {

                     showCommentView(image.getComment());

                 }

             }
         });

    }

    private void setUpUI(View view){

        imageView = view.findViewById(R.id.imageViewPreview);

        addCommentLayout = view.findViewById(R.id.addCommentLayout);
        viewCommentLayout = view.findViewById(R.id.viewCommentLayout);
        textViewComment = view.findViewById(R.id.textViewComment);
        btSubmitComment = view.findViewById(R.id.buttonSubmitComment);
        editTextComment = view.findViewById(R.id.editTextComment);


    }

    private void loadImage(String link){

        Glide.with(this)
                .load(link)
                .error(R.drawable.error_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .addListener(new RequestListener<Drawable>(){

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);

    }

    private void showCommentView(String comment){

        viewCommentLayout.setVisibility(View.VISIBLE);
        addCommentLayout.setVisibility(View.GONE);

        textViewComment.setText(comment);

    }

    private void showAddCommentView(){

        viewCommentLayout.setVisibility(View.GONE);
        addCommentLayout.setVisibility(View.VISIBLE);

        btSubmitComment.setOnClickListener(view -> {

            String comment = editTextComment.getText().toString();

            if (comment.isEmpty()){

                Toast.makeText(getActivity(),"Write something",Toast.LENGTH_LONG).show();

            }else {

                viewModel.addComment(id,link,comment);

            }

        });

    }
}