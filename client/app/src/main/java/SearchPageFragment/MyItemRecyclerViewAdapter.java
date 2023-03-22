package SearchPageFragment;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.playt.HomePageFragmentDirections;
import com.example.playt.R;
import com.example.playt.databinding.FragmentItemBinding;

import SearchPageFragment.placeholder.PlaceholderContent;
import SearchPageFragment.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyItemRecyclerViewAdapter(List<PlaceholderItem> items, Activity activity) {
        mValues = items;
        requireActivity = activity;
    }

    private Activity requireActivity;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImageView.setImageBitmap(mValues.get(position).image);
        holder.mContentView.setText(mValues.get(position).nickname);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("user_preferences", MODE_PRIVATE);


                    ItemFragmentDirections.ActionItemFragment2ToProfilePageFragment action =
                            ItemFragmentDirections.actionItemFragment2ToProfilePageFragment(mValues.get(holder.getBindingAdapterPosition()).username);

                    Navigation.findNavController(requireActivity, R.id.main_navhost)
                            .navigate(action);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mLayout;
        public final TextView mContentView;
        public final ImageView mImageView;
        public PlaceholderItem mItem;


        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mLayout = binding.itemLayout;
            mImageView = binding.searchUserImageView;
            mContentView = binding.content;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}