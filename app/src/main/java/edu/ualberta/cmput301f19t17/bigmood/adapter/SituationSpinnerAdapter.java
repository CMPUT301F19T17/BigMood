package edu.ualberta.cmput301f19t17.bigmood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

/**
 * This class serves as a custom ArrayAdapter specifically for SocialSituations.
 * This Adapter does the following:
 * 1) Stores a collection of SocialSituation objects in tandem with the ArrayList passed into its constructor.
 *    The first entry in the list is null, so that we can store a "Not Defined" SocialSituation, since this field is optional
 * 2) Inflates the different aspects of the row layout that are defined.
 */
public class SituationSpinnerAdapter extends ArrayAdapter<SocialSituation> {

    /**
     * This method prepares the List that will be used in the spinner adapter. We need to define the first element as null because we want to have an "optional" selection.
     * @return the prepared <code>List</code>
     */
    private static List<SocialSituation> initList() {

        // Create new list
        List<SocialSituation> spinnerList = new ArrayList<>();

        // Add optional and actual selections
        spinnerList.add(null);
        spinnerList.addAll(Arrays.asList(SocialSituation.values()));

        return spinnerList;

    }

    /**
     * Specialized constructor that defines a social situation adapter. It does not require a resource nor a object list because those are predefined and immutable.
     *
     * @param context  The current context.
     */
    public SituationSpinnerAdapter(@NonNull Context context) {
        super(context, 0, SituationSpinnerAdapter.initList());
    }

    /**
     * This method directly calls initView() in order to create the view used for drawing the spinner.
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
     * @param position    index of the item whose view we want.
     * @param convertView The view that is meant to be recycled. This value may be null.
     * @param parent      The attached parent that the adapter will attach to. This value must never be null.
     * @return            A View corresponding to the data at the specified position.
     */
    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.spinner_item_social_situation, parent, false);

        // Look up ImageView and TextView
        TextView situationText = convertView.findViewById(R.id.textview_social_situation);

        // Get Social Situation state from the super's collection list
        SocialSituation situation = this.getItem(position);

        // If the situation is null, we know it's the "no option". This should be displayed at the head of the list.
        if (situation != null)
            situationText.setText(situation.toString());
        else
            situationText.setText(R.string.placeholder_situation);

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
    public void remove(@Nullable SocialSituation object) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void add(@Nullable SocialSituation object) {
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
    public void addAll(@NonNull Collection<? extends SocialSituation> collection) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     * @throws UnsupportedOperationException if the underlying data collection is immutable
     */
    @Override
    public void addAll(SocialSituation... items) {
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
    public void insert(@Nullable SocialSituation object, int index) {
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
    public void sort(@NonNull Comparator<? super SocialSituation> comparator) {
        throw new UnsupportedOperationException("This is defined by an enumeration, and is technically immutable");
    }


}
