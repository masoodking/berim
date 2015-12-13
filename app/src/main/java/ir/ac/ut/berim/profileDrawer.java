package ir.ac.ut.berim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ir.ac.ut.models.FragmentIndex;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

/**
 * Created by saeed on 12/12/2015.
 */
public class profileDrawer extends MaterialNavigationDrawer implements MaterialSectionListener {

    public MaterialSection phoneSection;
        @Override
        public void init(Bundle savedInstanceState) {
            this.addSection(newSection("Section 1", new FragmentIndex()));
            Log.d("خر","خر");
        }
}