package com.example.lmohamed.criminalintent2;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by lmohamed on 2/22/17.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    /** explicitly tell FragmentManager that the Fragment should receive a call to onCreateOptionsMenu **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);     // create the RecyclerView
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));        // give the RecyclerView the LayoutManager object: it positions every item and defines how scrolling works

        updateUI();
        return view;
    }

    /** Reload the list after fragment is viewed and backed
     * not onStart() because we can't know the activity will
     * be stopped when another activity is in front of it
     * **/
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    /** menu **/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {    // Method
        super.onCreateOptionsMenu(menu, inflater);              // calls the inflater and pass the resource ID of the layout file
        inflater.inflate(R.menu.fragment_crime_list, menu);     // populates the menu instance with the items defined in the layout
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        // do something
        return false;
    }

    /** Implement the method updateUI that sets up CrimeListFragment's UI **/
    private void updateUI() {
        CrimeLab crimelab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimelab.getCrimes();

        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    /** Define the ViewHolder as an inner class **/
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        /**
         * the constructor inflates list_item_crime
         * which gets immediately passed into super(...), ViewHolder's constructor
         * the layout is held in ViewHolder's itemView field
         */
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            // TODO 1: figure out the first challenge. How does the correct layout get here?

            itemView.setOnClickListener(this);      // itemView is the View for the entire row.
            //itemView.findViewById(R.id.crime_title).setOnClickListener(this); <<-- specify the Title TextView

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        /** Make the ViewHolder the onClickListener for its View  **/
        @Override
        public void onClick(View view) {
            /** Store the crime ID in the intent that belongs to CrimePagerActivity.java
             * -- Pass in the context and the crime ID
             * -- CrimeFragment needs to retrieve and use this data
             */
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }

        /** The bind(Crime) method will be called each time a new
         * Crime should be displayed in CrimeHolder
         * (used in onBindViewHolder)
         */
        private void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            // TODO: format to include HH:MM
            mDateTextView.setText(android.text.format.DateFormat.format("EEEE MMM dd, yyy", mCrime.getDate())); //mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
//          int layout;
           // mRequiresPoliceButton.setVisibility(View.VISIBLE);
//
//            if(viewType == 1){
//                layout = R.layout.list_item_crime_police;
//            } else {
//
//                layout = R.layout.list_item_crime;
//            }
//            return layout;
        }
    }

    /** Create the adapter **/
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;        // list of Crimes

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        } // constructor

        @Override
        public int getItemCount() {         // Return the size of list (invoked by the layout manager)
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {  // return the type of view that will be created by getView
            Crime crime = mCrimes.get(position);
            int viewType = crime.getType();
            return viewType;
        }

        /**
        // Get a view that dipslays data in the specified position
        @Override
        public View getView(int position, View convertview, ViewGroup parent) {

        }

         */
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CrimeHolder crimeHolder;

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch(viewType){
                case 0:
                    layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                    crimeHolder = new CrimeHolder(layoutInflater, parent);
                    break;
                case 1:
                    layoutInflater.inflate(R.layout.list_item_crime_police, parent, false);
                    crimeHolder = new CrimeHolder(layoutInflater, parent);
                    break;
                default:
                    layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                    crimeHolder = new CrimeHolder(layoutInflater, parent);
                    break;
            }
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            /** Each time the Recyclerview requests that a given CrimeHolder
             *  be bound to a particular crime, bind(Crime) is called
             */
            Crime crime = mCrimes.get(position);
            //int viewType = holder.getItemViewType();
            holder.bind(crime);

        }

    }



}
