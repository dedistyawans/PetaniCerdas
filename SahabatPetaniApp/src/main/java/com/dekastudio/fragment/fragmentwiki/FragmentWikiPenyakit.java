package com.dekastudio.fragment.fragmentwiki;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dekastudio.adapter.PenyakitAdapter;
import com.dekastudio.core.AppCore;
import com.dekastudio.helper.Constant;
import com.dekastudio.helper.JarakRecycleView;
import com.dekastudio.sahabatpetanicerdas.R;

public class FragmentWikiPenyakit extends Fragment{

    PenyakitAdapter penyakitAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.penyakit_wiki_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        //assertion utk cek debug kondisi tidak null, jika null akan Throw AssertionError
        Bundle bundle = getArguments();
        assert bundle != null;
        penyakitAdapter = new PenyakitAdapter(getActivity(), bundle.getBoolean(Constant.INDEX_CODE), ((AppCore)getActivity().getApplication()).getRequestQueue(), root.findViewById(R.id.txtKosongBookmarkPenyakit));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new JarakRecycleView(10, getActivity()));
        recyclerView.setAdapter(penyakitAdapter);
        penyakitAdapter.notifyDataSetChanged();


        SearchView searchView = root.findViewById(R.id.search);
        searchView.setQueryHint("Cari hama...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                penyakitAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                penyakitAdapter.filter(newText);
                return false;
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    public void notifyDataBerubah(int idPenyakit){
        penyakitAdapter.notifyDataBerubah(idPenyakit);
    }
}
