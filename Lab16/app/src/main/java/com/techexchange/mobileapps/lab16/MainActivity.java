package com.techexchange.mobileapps.lab16;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoAdapter.PhotoHolder> mThumbnailDownloader;
    private static Context mainActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityContext = this;

        mPhotoRecyclerView = findViewById(R.id.recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setupAdapter();

        AsyncTask<Void, Void, List<GalleryItem>> task = new AsyncTask<Void, Void, List<GalleryItem>>() {
            @Override
            protected List<GalleryItem> doInBackground(Void... voids) {
                return new HttpRequester().getFlickrPhotos();
            }

            @Override
            protected void onPostExecute(List<GalleryItem> galleryItems) {
                super.onPostExecute(galleryItems);
                mItems = galleryItems;
                setupAdapter();
            }
        };

        task.execute();

        Handler responseHanlder = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHanlder);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoAdapter.PhotoHolder>(){

                    @Override
                    public void onThumbnailDownloaded(PhotoAdapter.PhotoHolder target, Bitmap thumbnail) {
                        Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                        target.bindDrawable(drawable);
                    }
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    private void setupAdapter(){
        mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

        private static final String TAG = "PhotoAdapter";
        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> mGalleryItems) {
            this.mGalleryItems = mGalleryItems;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.mainActivityContext);
            View view = inflater.inflate(R.layout.image_fragment, viewGroup, false);
            return  new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int i) {
            GalleryItem galleryItem = mGalleryItems.get(i);
            Drawable placeholder = getResources().getDrawable(R.drawable.brick);
            photoHolder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl());
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        class PhotoHolder extends RecyclerView.ViewHolder{

            private ImageView mItemImageView;

            PhotoHolder(View itemView) {
                super(itemView);
                mItemImageView = (ImageView) itemView.findViewById(R.id.image_placement);
            }

            public void bindDrawable(Drawable drawable){
                mItemImageView.setImageDrawable(drawable);
            }
        }
    }
}
