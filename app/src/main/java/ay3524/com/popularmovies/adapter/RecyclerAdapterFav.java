package ay3524.com.popularmovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette; // Updated from androidx.appcompat.graphics.Palette
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ay3524.com.popularmovies.R;
import ay3524.com.popularmovies.Utils.BitmapUtility;
import ay3524.com.popularmovies.model.MovieFav;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Ashish on 22-10-2016.
 */


public class RecyclerAdapterFav extends RecyclerView.Adapter<RecyclerAdapterFav.ViewHolder> {

    private List<MovieFav> itemsModels;
    private final Context context;
    private ClickListener clickListener;

    public RecyclerAdapterFav(List<MovieFav> itemsModels, Context context) {
        this.itemsModels = itemsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        item_card_girdlayout binding = ItemCardGridLayoutBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieFav movie = itemsModels.get(position);

        byte[] posterImage = movie.getPosterImage();
        holder.binding.image.setImageBitmap(BitmapUtility.getImage(posterImage));
        holder.binding.movieItemTitle.setText(movie.getName());
        holder.binding.movieItemGenres.setText(movie.getGenre());

        BitmapDrawable drawable = (BitmapDrawable) holder.binding.image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Palette palette = Palette.from(bitmap).generate();
        int defaultColor = 0xFF333333;
        int color = palette.getDarkMutedColor(defaultColor);
        holder.binding.movieItemFooter.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return itemsModels == null ? 0 : itemsModels.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemCardGridLayoutBinding binding;

        ViewHolder(ItemCardGridLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface ClickListener {
        void onItemClick(View v, int pos);
    }
}

