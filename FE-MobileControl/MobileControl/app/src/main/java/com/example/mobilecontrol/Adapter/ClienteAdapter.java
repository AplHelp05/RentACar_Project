package com.example.mobilecontrol.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilecontrol.LogicaNegocio.Cliente;
import com.example.mobilecontrol.R;
import com.example.mobilecontrol.LogicaNegocio.Cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.MyViewHolder> implements Filterable {
    private List<Cliente> clienteList;
    private List<Cliente> clienteListFiltered;
    private ClienteAdapterListener listener;
    private Cliente deletedItem;

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
                    listener.onContactSelected(clienteListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public ClienteAdapter(List<Cliente> clienteList, ClienteAdapterListener listener) {
        this.clienteList = clienteList;
        this.listener = listener;
        //init filter
        this.clienteListFiltered = clienteList;
    }

    public void updateClientes(List<Cliente> clientes) {
        this.clienteList = clientes;
        notifyDataSetChanged();
    }

    @Override
    public ClienteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClienteAdapter.MyViewHolder holder, int position) {
        // rendering view
        final Cliente cliente = clienteListFiltered.get(position);
        holder.titulo1.setText(cliente.getCedula());
        holder.titulo2.setText(cliente.getNombre());
        holder.description.setText(cliente.getEmail());
    }

    @Override
    public int getItemCount() {
        return clienteListFiltered.size();
    }

    public void removeItem(int position) {
        deletedItem = clienteListFiltered.remove(position);
        Iterator<Cliente> iter = clienteList.iterator();
        while (iter.hasNext()) {
            Cliente aux = iter.next();
            if (deletedItem.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (clienteListFiltered.size() == clienteList.size()) {
            clienteListFiltered.add(position, deletedItem);
        } else {
            clienteListFiltered.add(position, deletedItem);
            clienteList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Cliente getSwipedItem(int index) {
        if (this.clienteList.size() == this.clienteListFiltered.size()) { //not filtered yet
            return clienteList.get(index);
        } else {
            return clienteListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (clienteList.size() == clienteListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(clienteList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(clienteList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(clienteListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(clienteListFiltered, i, i - 1);
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
                    clienteListFiltered = clienteList;
                } else {
                    List<Cliente> filteredList = new ArrayList<>();
                    for (Cliente row : clienteList) {
                        // filter use two parameters
                        if (row.getCedula().toLowerCase().contains(charString.toLowerCase()) || row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    clienteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = clienteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clienteListFiltered = (ArrayList<Cliente>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ClienteAdapterListener {
        void onContactSelected(Cliente cliente);
    }
}
