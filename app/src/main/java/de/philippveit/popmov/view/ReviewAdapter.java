package de.philippveit.popmov.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.philippveit.popmov.R;
import de.philippveit.popmov.data.Review;

/**
 * Created by Philipp on 28.02.2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewReviewUser)
        public TextView username;

        @BindView(R.id.textViewReviewContent)
        public ReadMoreTextView content;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public Context mContext;
    public List<Review> reviewList;

    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_review, parent, false);
        ButterKnife.bind(this, itemView);
        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        Review review = reviewList.get(adapterPosition);
        holder.username.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


}
