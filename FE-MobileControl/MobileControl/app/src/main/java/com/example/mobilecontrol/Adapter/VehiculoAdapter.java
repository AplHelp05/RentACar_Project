package com.example.mobilecontrol.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilecontrol.LogicaNegocio.Vehiculo;
import com.example.mobilecontrol.R;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.MyViewHolder> implements Filterable {
    private List<Vehiculo> vehiculoList;
    private List<Vehiculo> vehiculoListFiltered;
    private VehiculoAdapterListener listener;
    private Vehiculo deletedItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo1, titulo2, description;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;


        public MyViewHolder(View view) {
            super(view);
            titulo1 = view.findViewById(R.id.titleFirstLbl);
            titulo2 = view.findViewById(R.id.titleSecLbl);
            description = view.findViewById(R.id.descriptionLbl);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(vehiculoListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public VehiculoAdapter(List<Vehiculo> agenciaList, VehiculoAdapterListener listener) {
        this.vehiculoList = agenciaList;
        this.listener = listener;
        //init filter
        this.vehiculoListFiltered = agenciaList;
    }

    @Override
    public VehiculoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VehiculoAdapter.MyViewHolder holder, int position) {
        // rendering view
        final Vehiculo cliente = vehiculoListFiltered.get(position);
        holder.titulo1.setText(cliente.getCodigo());
        holder.titulo2.setText(cliente.getPlaca());
        holder.description.setText(cliente.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return vehiculoListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    vehiculoListFiltered = vehiculoList;
                } else {
                    List<Vehiculo> filteredList = new ArrayList<>();
                    for (Vehiculo row : vehiculoList) {
                        // filter use two parameters
                        if (row.getCodigo().toLowerCase().contains(charString.toLowerCase()) || row.getPlaca().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    vehiculoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = vehiculoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                vehiculoListFiltered = (ArrayList<Vehiculo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void removeItem(int position) {
        deletedItem = vehiculoListFiltered.remove(position);
        Iterator<Vehiculo> iter = vehiculoList.iterator();
        while (iter.hasNext()) {
            Vehiculo aux = iter.next();
            if (deletedItem.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (vehiculoListFiltered.size() == vehiculoList.size()) {
            vehiculoListFiltered.add(position, deletedItem);
        } else {
            vehiculoListFiltered.add(position, deletedItem);
            vehiculoList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Vehiculo getSwipedItem(int index) {
        if (this.vehiculoList.size() == this.vehiculoListFiltered.size()) { //not filtered yet
            return vehiculoList.get(index);
        } else {
            return vehiculoListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (vehiculoList.size() == vehiculoListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(vehiculoList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(vehiculoList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(vehiculoListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(vehiculoListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }



    public void updateAgencias(List<Vehiculo> vehiculos) {
        this.vehiculoList = vehiculos;
        notifyDataSetChanged();
    }

    public interface VehiculoAdapterListener {
        void onContactSelected(Vehiculo vehiculo);
    }
}
