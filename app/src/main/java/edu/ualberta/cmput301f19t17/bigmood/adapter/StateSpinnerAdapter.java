package edu.ualberta.cmput301f19t17.bigmood.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;

/**
 * This class provides a ArrayAdapter for the mood/state spinner so that it can display the mood emoticons.
 */
public class StateSpinnerAdapter extends ArrayAdapter<EmotionalState> {

    /**
     * Specialized constructor that defines a social situation adapter. It does not require a resource nor a object list because those are predefined and immutable.
     *
     * @param context  The current context.
     */
    public StateSpinnerAdapter(@NonNull Context context) {
        super(context, 0, Arrays.asList(EmotionalState.values()));
    }

    /**
     * This method directly calls initView() in order to create the view used for drawing the spinner.
     *
     * @param position    index of the item whose view we want.
     * @param convertView The view that is meant to be recycled. This value may be null.
     * @param parent      The attached parent that the adapter will attach to. This value must never be null.
     * @return            A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return this.initView(position, convertView, parent);
    }

    /**
     * This method directly calls initView() in order to create the view used for drawing the spinner.
     *
     * @param position    index of the item whose view we want.
     * @param convertView The view that is meant to be recycled. This value may be null.
     * @param parent      The attached parent that the adapter will attach to. This value must never be null.
     * @return            A View corresponding to the data at the specified position.
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return this.initView(position, convertView, parent);
    }

    /**
     * This method inflates the view of the spinner and returns a view for which the caller can use to draw the spinner.
     *
     * @param position    index of the item whose view we want.
     * @param convertView The view that is meant to be recycled. This value may be null.
     * @param parent      The attached parent that the adapter will attach to. This value must never be null.
     * @return            A View corresponding to the data at the specified position.
     */
    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.spinner_item_mood, parent, false);

        // Look up ImageView and TextView
        ImageView image = convertView.findViewById(R.id.imageview_state);
        TextView stateText = convertView.findViewById(R.id.textview_state);

        // Get Emotional state from the super's collection list
        EmotionalState state = this.getItem(position);

        // To be safe, check if state is null.
        if (state != null) {

            // Get resources for creating a drawable
            Resources res = this.getContext().getResources();

            // Set image and text
            image.setImageDrawable(res.getDrawable(state.getDrawableId()));
            stateText.setText(state.toString());

        }

        return convertView;

    }


    // Must disable all these operations because the dataset is immutable //


    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void remove(@Nullable EmotionalState object) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void add(@Nullable EmotionalState object) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     */
    @Override
    public void addAll(@NonNull Collection<? extends EmotionalState> collection) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void addAll(EmotionalState... items) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void insert(@Nullable EmotionalState object, int index) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Remove all elements from the list.
     *
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *                   in this adapter.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void sort(@NonNull Comparator<? super EmotionalState> comparator) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

}