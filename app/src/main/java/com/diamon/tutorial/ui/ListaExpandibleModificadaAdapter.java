package com.diamon.tutorial.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.List;

public class ListaExpandibleModificadaAdapter extends BaseExpandableListAdapter {

    private final Context contexto;

    private final HashMap<ViewGroup, List<ViewGroup>> listaInterna;

    private final List<ViewGroup> listaExterna;

    public ListaExpandibleModificadaAdapter(
            Context contexto,
            HashMap<ViewGroup, List<ViewGroup>> listaInterna,
            List<ViewGroup> listaExterna) {
        this.contexto = contexto;

        this.listaInterna = listaInterna;

        this.listaExterna = listaExterna;
    }

    @Override
    public int getGroupCount() {

        return listaExterna.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return listaInterna.get(listaExterna.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return listaExterna.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return listaInterna.get(listaExterna.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View vista, ViewGroup diseno) {
        diseno = (ViewGroup) getGroup(groupPosition);

        return diseno;
    }

    @Override
    public View getChildView(
            int groupPosition,
            int childPosition,
            boolean isLastChild,
            View vista,
            ViewGroup diseno) {
        diseno = (ViewGroup) getChild(groupPosition, childPosition);

        return diseno;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
}
