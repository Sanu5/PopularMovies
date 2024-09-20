package ay3524.com.popularmovies.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ay3524.com.popularmovies.R;
import ay3524.com.popularmovies.Utils.Constants;
import ay3524.com.popularmovies.fragments.MoviesDetailsFavFragment;
import ay3524.com.popularmovies.fragments.MoviesFavoriteListFragment;

/**
 * Created by Ashish on 02-12-2016.
 */

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

public class FavActivity extends AppCompatActivity implements MoviesFavoriteListFragment.OnHeadLineSelectedListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fav);

        if (findViewById(R.id.content1) != null) {
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of the Fragment Class which has the list i.e. MoviesFavoriteListFragment
            MoviesFavoriteListFragment favoriteFragment = new MoviesFavoriteListFragment();

            // Pass the Intent's extras to the fragment as arguments if there are any
            favoriteFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the activity using FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content1, favoriteFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onArticleSelected(int position) {
        // Capture the MoviesDetailsFavFragment from the activity's dual-pane layout
        MoviesDetailsFavFragment articleFragment = (MoviesDetailsFavFragment) getSupportFragmentManager()
                .findFragmentById(R.id.articlesfragment1);

        // Check if the fragment exists (indicating two-pane mode)
        if (articleFragment != null) {
            // In two-pane layout, update the article view
            articleFragment.updateArticleView(position);
        } else {
            // In one-pane layout, swap fragments
            MoviesDetailsFavFragment swapFragment = new MoviesDetailsFavFragment();
            Bundle args = new Bundle();
            args.putInt(Constants.ARG_POSITION, position);
            swapFragment.setArguments(args);

            // Replace the fragment using FragmentManager
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content1, swapFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}

