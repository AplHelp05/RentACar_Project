package com.example.mobilecontrol.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.mobilecontrol.LogicaNegocio.Reserva;
import com.example.mobilecontrol.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.MyViewHolder> implements Filterable {
    private List<Reserva> reservaList;
    private List<Reserva> reservaListFiltered;
    private ReservaAdapterListener listener;
    private Reserva deletedItem;



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
                    listener.onContactSelected(reservaListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public ReservaAdapter(List<Reserva> reservaList, ReservaAdapterListener listener) {
        this.reservaList = reservaList;
        this.listener = listener;
        //init filter
        this.reservaListFiltered = reservaList;
    }

    public void updateReservas(List<Reserva> reservas) {
        this.reservaList = reservas;
        notifyDataSetChanged();
    }

    @Override
    public ReservaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReservaAdapter.MyViewHolder holder, int position) {
        // rendering view
        final Reserva cliente = reservaListFiltered.get(position);
        holder.titulo1.setText(cliente.getCodigo());
        holder.titulo2.setText(cliente.getAgencia().getNombre());
        holder.description.setText(cliente.getVehiculo().getDescripcion());
    }

    @Override
    public int getItemCount() {
        return reservaListFiltered.size();
    }


    public void removeItem(int position) {
        deletedItem = reservaListFiltered.remove(position);
        Iterator<Reserva> iter = reservaList.iterator();
        while (iter.hasNext()) {
            Reserva aux = iter.next();
            if (deletedItem.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (reservaListFiltered.size() == reservaList.size()) {
            reservaListFiltered.add(position, deletedItem);
        } else {
            reservaListFiltered.add(position, deletedItem);
            reservaList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Reserva getSwipedItem(int index) {
        if (this.reservaList.size() == this.reservaListFiltered.size()) { //not filtered yet
            return reservaList.get(index);
        } else {
            return reservaListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (reservaList.size() == reservaListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(reservaList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(reservaList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(reservaListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(reservaListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    reservaListFiltered = reservaList;
                } else {
                    List<Reserva> filteredList = new ArrayList<>();
                    for (Reserva row : reservaList) {
                        // filter use two parameters
                        if (row.getCodigo().toLowerCase().contains(charString.toLowerCase()) || row.getFecha_inicio().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    reservaListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = reservaListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                reservaListFiltered = (ArrayList<Reserva>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ReservaAdapterListener {
        void onContactSelected(Reserva reserva);
    }

}
