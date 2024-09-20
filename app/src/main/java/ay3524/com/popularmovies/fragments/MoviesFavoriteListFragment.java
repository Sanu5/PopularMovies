package ay3524.com.popularmovies.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ay3524.com.popularmovies.R;
import ay3524.com.popularmovies.Utils.Constants;
import ay3524.com.popularmovies.Utils.SpacesItemDecoration;
import ay3524.com.popularmovies.adapter.RecyclerAdapterFav;
import ay3524.com.popularmovies.data.MovieContract;
import ay3524.com.popularmovies.data.MovieDbHelper;
import ay3524.com.popularmovies.model.MovieFav;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ay3524.com.popularmovies.Utils.Constants.MOVIES_LOADER_ID_ONE;
import static ay3524.com.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by Ashish on 02-11-2016.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MoviesFavoriteListFragment extends Fragment implements RecyclerAdapterFav.ClickListener {

    private GridLayoutManager layoutManager;
    private RecyclerAdapterFav adapter;
    private List<MovieFav> itemsModels = new ArrayList<>();
    private SQLiteDatabase database;
    private MovieDbHelper dbHelper;
    private OnHeadLineSelectedListener callback;
    private View emptyView;
    private Toolbar tb;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_fav_layout, container, false);

        // Initialize views
        emptyView = v.findViewById(R.id.empty_view);
        tb = v.findViewById(R.id.toolbar);
        recyclerView = v.findViewById(R.id.recyclerView);

        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);

        emptyView.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), Constants.GRID_TWO);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), Constants.GRID_THREE);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(MOVIES_LOADER_ID_ONE, MOVIES_LOADER_ID_ONE, false));

        dbHelper = new MovieDbHelper(getActivity());
        database = dbHelper.getReadableDatabase();
        itemsModels = getListFromDb(database);

        adapter = new RecyclerAdapterFav(itemsModels, getActivity());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        if (itemsModels.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        }

        return v;
    }

    public interface OnHeadLineSelectedListener {
        void onArticleSelected(int position);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callback = (OnHeadLineSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHeadLineSelectedListener");
        }
    }

    @Override
    public void onItemClick(View v, int pos) {
        MovieFav currentMovies = itemsModels.get(pos);
        MoviesDetailsFavFragment.newFavInstance(
                currentMovies.getPosterImage(),
                currentMovies.getPlot(),
                currentMovies.getReleaseDate(),
                currentMovies.getName(),
                currentMovies.getRating(),
                currentMovies.getVideoImage(),
                currentMovies.getGenre(),
                currentMovies.getTrailer(),
                currentMovies.getVoteCount()
        );
        callback.onArticleSelected(pos);
    }

    private List<MovieFav> getListFromDb(SQLiteDatabase database) {
        List<MovieFav> list = new ArrayList<>();
        String[] projection = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
                MovieContract.MovieEntry.COLUMN_MOVIE_GENRE,
                MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER,
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
                MovieContract.MovieEntry.COLUMN_MOVIE_VIDEO_POSTER,
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_MOVIE_RATING,
                MovieContract.MovieEntry.COLUMN_MOVIE_PLOT,
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT
        };
        Cursor cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, projection, null, null, null, null, null);

        int columnIndexMovieName = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME);
        int columnIndexMovieGenre = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_GENRE);
        int columnIndexMovieTrailer = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER);
        int columnIndexMoviePoster = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
        int columnIndexMovieVideoPoster = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VIDEO_POSTER);
        int columnIndexMovieReleaseDate = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        int columnIndexMovieRating = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
        int columnIndexMoviePlot = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_PLOT);
        int columnIndexMovieVoteCount = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT);

        while (cursor.moveToNext()) {
            MovieFav movieFav = new MovieFav();
            movieFav.setName(cursor.getString(columnIndexMovieName));
            movieFav.setGenre(cursor.getString(columnIndexMovieGenre));
            movieFav.setTrailer(cursor.getString(columnIndexMovieTrailer));
            movieFav.setPosterImage(cursor.getBlob(columnIndexMoviePoster));
            movieFav.setVideoImage(cursor.getBlob(columnIndexMovieVideoPoster));
            movieFav.setReleaseDate(cursor.getString(columnIndexMovieReleaseDate));
            movieFav.setRating(cursor.getString(columnIndexMovieRating));
            movieFav.setPlot(cursor.getString(columnIndexMoviePlot));
            movieFav.setVoteCount(cursor.getString(columnIndexMovieVoteCount));
            list.add(movieFav);
        }
        cursor.close();
        return list;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fav, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_delete) {
            database = dbHelper.getReadableDatabase();
            long cnt = DatabaseUtils.queryNumEntries(database, MovieContract.MovieEntry.TABLE_NAME);
            if (cnt > 0) {
                showDeleteDialog();
            } else {
                Toast.makeText(getActivity(), "No Movies to delete :(", Toast.LENGTH_SHORT).show();
            }
            database.close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("This action will delete all your favorite movies")
                .setPositiveButton("Delete", (dialog, id) -> {
                    long rows = deleteAllMovies();
                    if (rows != 0) {
                        Toast.makeText(getActivity(), rows + " Movies Deleted :)", Toast.LENGTH_SHORT).show();
                    }
                    itemsModels.clear();
                    adapter.notifyDataSetChanged();
                    if (adapter == null) {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private long deleteAllMovies() {
        database = dbHelper.getWritableDatabase();
        long rows = database.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
        emptyView.setVisibility(View.VISIBLE);
        return rows;
    }
}
