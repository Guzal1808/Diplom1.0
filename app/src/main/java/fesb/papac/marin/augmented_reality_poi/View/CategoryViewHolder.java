package fesb.papac.marin.augmented_reality_poi.View;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import fesb.papac.marin.augmented_reality_poi.Model.CategoryPlace;
import fesb.papac.marin.augmented_reality_poi.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class CategoryViewHolder extends GroupViewHolder {

    private TextView categoryName;
    private ImageView arrow;
    private ImageView icon;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        this.categoryName = (TextView) itemView.findViewById(R.id.list_item_genre_name);
        this.arrow = (ImageView) itemView.findViewById(R.id.list_item_genre_arrow);
        this.icon = (ImageView) itemView.findViewById(R.id.list_item_genre_icon);
    }

    public void setCategoryTitle(ExpandableGroup category)
    {
        categoryName.setText(category.getTitle());
        icon.setBackgroundResource(((CategoryPlace) category).getIconResId());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
