package com.example.dfrie.tweeterphile.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.TwitterApplication;
import com.example.dfrie.tweeterphile.activities.TimelineActivity;
import com.example.dfrie.tweeterphile.restclient.TwitterClient;
import com.example.dfrie.tweeterphile.restclient.models.Settings;
import com.example.dfrie.tweeterphile.restclient.models.User;
import com.example.dfrie.tweeterphile.utils.TwitterUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.StringUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.loopj.android.http.AsyncHttpClient.log;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    private static String NULL_INDICATOR = "null";

    private TwitterClient client;

    private User currentUser;
    private RelativeLayout profileLayout;
    private String screenName = null;

    private TextView tvToolbarTitle;
    private TextView tvErrorMsg;

    private ImageView ivProfilePic;
    private TextView tvName;
    private TextView tvComment;
    private TextView tvLocation;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private TextView tvBullet;
    private TextView tvFollowersCount;
    private TextView tvFollowingCount;

    //private OnFragmentInteractionListener mListener;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param screenName
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String screenName) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(TimelineActivity.EXTRA_SCREEN_ID, screenName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * EsXpose the user.
     * @return
     */
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            screenName = getArguments().getString(TimelineActivity.EXTRA_SCREEN_ID);
        }
        ////screenName = getIntent().getStringExtra(EXTRA_SCREEN_ID);

        client = TwitterApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        setupProfileHeader(view);
        return view;
    }

    private void setupProfileHeader(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.includedToolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        tvToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvComment = (TextView) view.findViewById(R.id.tvComment);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        tvFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        tvBullet = (TextView) view.findViewById(R.id.tvBullet);
        tvFollowersCount = (TextView) view.findViewById(R.id.tvFollowersCount);
        tvFollowingCount = (TextView) view.findViewById(R.id.tvFollowingCount);
        tvErrorMsg = (TextView) view.findViewById(R.id.tvErrorMsg);
        profileLayout = (RelativeLayout) view.findViewById(R.id.profileLayout);

        if (screenName != null) {
            getUserDetailsForScreenName(screenName);
        } else {
            // GET account/settings to get screen name
            client.getAccountSettings(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Settings settings = new Settings(response);
                    getUserDetailsForScreenName(settings.getScreenName());
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    log.d("debug", errorResponse.toString());
                    log.d("debug", throwable.getMessage());
                    tvErrorMsg.setText(throwable.getMessage());
                }
            });
        }
    }

    private void getUserDetailsForScreenName(String screenName) {
        client.getUserDetails(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<User> users = User.fromJsonArray(response);
                currentUser = users.get(0);
                tvToolbarTitle.setText("@" + currentUser.getScreenName());

                tvName.setText(currentUser.name);
                if (StringUtils.isNotNullOrEmpty(currentUser.description)) {
                    tvComment.setText(currentUser.description);
                } else {
                    tvComment.setVisibility(View.GONE);
                }
                if (StringUtils.isNotNullOrEmpty(currentUser.location)) {
                    tvLocation.setText(currentUser.location);
                } else {
                    tvLocation.setVisibility(View.GONE);
                }
                tvFollowersCount.setText(TwitterUtils.formatInteger(currentUser.followersCount));
                tvFollowingCount.setText(TwitterUtils.formatInteger(currentUser.friendsCount));

                if (StringUtils.isNullOrEmpty(currentUser.profileBackgroundImageUrl) ||
                        currentUser.profileBackgroundImageUrl.equals(NULL_INDICATOR)) {
                    profileLayout.setBackgroundColor(Integer.parseInt(currentUser.profileBackgroundColor, 16));
                } else {
/*
                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            // Bitmap is loaded, use image here
                            BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                            profileLayout.setBackground(drawable);
                        }
                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            profileLayout.setBackgroundColor(Integer.parseInt(currentUser.profileBackgroundColor, 16));
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    };
                    Picasso.with(getContext()).load(currentUser.profileBackgroundImageUrl)
                            .resize(profileLayout.getWidth(), profileLayout.getHeight())
                            .into(target);
*/
                    new GetImageFromServer().execute(currentUser.profileBackgroundImageUrl);
                }

                ////profileLayout.setBackgroundColor(Integer.parseInt(currentUser.profileBackgroundColor, 16));
/*
                int bc = Integer.parseInt(currentUser.profileTextColor);
                tvName.setTextColor(bc);
                tvComment.setTextColor(bc);
                tvErrorMsg.setTextColor(bc);
                tvFollowersCount.setTextColor(bc);
                tvFollowingCount.setTextColor(bc);
*/

                Picasso.with(getContext()).load(currentUser.profileImageUrl)
                        .placeholder(R.drawable.ic_twitter)
                        .resize(300, 300)
                        .transform(new RoundedCornersTransformation(20, 20))
                        .into(ivProfilePic);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                log.d("debug", errorResponse.toString());
                log.d("debug", throwable.getMessage());
                tvErrorMsg.setText(throwable.getMessage());
            }
        });
    }

    public class GetImageFromServer extends AsyncTask<String, Void, Bitmap> {
        private Bitmap bitmap;
        int h, w;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            h = profileLayout.getHeight();
            w = profileLayout.getWidth();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL urli = new URL(params[0].trim());
                URLConnection ucon = urli.openConnection();
                BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inSampleSize = 2;
                //options.inScaled = false;
                //options.outHeight = h;
                //options.outWidth = w;
                //options.inJustDecodeBounds = true;
                bitmap = BitmapFactory.decodeStream(ucon.getInputStream(), null, options);
                // Crop the image to the  available layout dimensions...
                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        Math.min(w, bitmap.getWidth()), Math.min(h, bitmap.getHeight()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                profileLayout.setBackgroundColor(Integer.parseInt(currentUser.profileBackgroundColor, 16));
                return;
            }
            BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
            profileLayout.setBackground(drawable);
            int colorInt = (isBitmapDark(bitmap)? 0xffffffff: 0xff000000);
            tvErrorMsg.setTextColor(colorInt);
            tvName.setTextColor(colorInt);
            tvComment.setTextColor(colorInt);
            tvLocation.setTextColor(colorInt);
            tvFollowers.setTextColor(colorInt);
            tvFollowing.setTextColor(colorInt);
            tvBullet.setTextColor(colorInt);
            tvFollowersCount.setTextColor(colorInt);
            tvFollowingCount.setTextColor(colorInt);
        }

        private boolean isBitmapDark(Bitmap bitmap) {
            long redBucket = 0;
            long greenBucket = 0;
            long blueBucket = 0;
            long pixelCount = bitmap.getHeight() * bitmap.getWidth();

            for (int y = 0; y < bitmap.getHeight(); y++) {
                for (int x = 0; x < bitmap.getWidth(); x++) {
                    int c = bitmap.getPixel(x, y);
                    redBucket += Color.red(c);
                    greenBucket += Color.green(c);
                    blueBucket += Color.blue(c);
                }
            }
            return (((redBucket / pixelCount +
                    greenBucket / pixelCount +
                    blueBucket / pixelCount) /3) < 128);
        }
    }



/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
*/

}
