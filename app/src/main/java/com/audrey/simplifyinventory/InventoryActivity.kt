package com.audrey.simplifyinventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.audrey.simplifyinventory.helpers.LoginHelpers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.audrey.simplifyinventory.entity.ItemType
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.audrey.simplifyinventory.entity.InventoryItem

class InventoryActivity : ComponentActivity() {
    private lateinit var adapter: GridAdapter
    private var isEditMode = false
    enum class SortBy {
        QUANTITY_HIGH,
        QUANTITY_LOW,
        NAME_AZ,
        NAME_ZA,
        RECENTLY_ADDED,
        OLDEST_ADDED
    }

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory)

        val gridView = findViewById<GridView>(R.id.InventoryGrid)
        val db = AppDatabase.getDatabase(this)
        val userId = LoginHelpers.getLoggedInUserId(this@InventoryActivity)
        val items = runBlocking {
            db.itemTypeDao()
                .getItemTypesForUser(userId)
        }
        adapter = GridAdapter(this, items)
        gridView.adapter = adapter

        val editBtn = findViewById<Button>(R.id.buttonEdit)

        editBtn.setOnClickListener {
            isEditMode = !isEditMode
            editBtn.text = if (isEditMode) "Done" else "Edit"
            adapter.setEditMode(isEditMode)
        }

        val sortQuantityBtn = findViewById<Button>(R.id.sort_quantity)
        val sortNameBtn = findViewById<Button>(R.id.sort_name)
        val sortRecentBtn = findViewById<Button>(R.id.sort_recent)

        sortQuantityBtn.setOnClickListener {
            adapter.sortBy = if (adapter.sortBy == SortBy.QUANTITY_HIGH) SortBy.QUANTITY_LOW else SortBy.QUANTITY_HIGH
            sortQuantityBtn.text = if (adapter.sortBy == SortBy.QUANTITY_HIGH) "Quantity ↓" else "Quantity ↑"
            sortNameBtn.text = "Name"
            sortRecentBtn.text = "Recent"
            adapter.updateData()
        }

        sortNameBtn.setOnClickListener {
            adapter.sortBy = if (adapter.sortBy == SortBy.NAME_AZ) SortBy.NAME_ZA else SortBy.NAME_AZ
            sortNameBtn.text = if (adapter.sortBy == SortBy.NAME_AZ) "Name A→Z" else "Name Z→A"
            sortQuantityBtn.text = "Quantity"
            sortRecentBtn.text = "Recent"
            adapter.updateData()
        }

        sortRecentBtn.setOnClickListener {
            adapter.sortBy = if (adapter.sortBy == SortBy.RECENTLY_ADDED) SortBy.OLDEST_ADDED else SortBy.RECENTLY_ADDED
            sortRecentBtn.text = if (adapter.sortBy == SortBy.RECENTLY_ADDED) "Recent ↓" else "Recent ↑"
            sortQuantityBtn.text = "Quantity"
            sortNameBtn.text = "Name"
            adapter.updateData()
        }

        findViewById<Button>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<Button>(R.id.nav_inventory).setOnClickListener {
        }

        findViewById<Button>(R.id.nav_shop).setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        findViewById<Button>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }

        findViewById<EditText>(R.id.search_bar).addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("OnResume", "called")
    }

    fun logOut(view: View) {
        LoginHelpers.logOut(this)
        finish()
    }

    fun addItem(view: View) {
        val addItemView = layoutInflater.inflate(R.layout.add_item, null)
        val dialog = AlertDialog.Builder(this)
            .setView(addItemView)
            .create()

        val addButton = addItemView.findViewById<Button>(R.id.addItemButton)
        val cancelButton = addItemView.findViewById<Button>(R.id.cancelButton)

        addButton.setOnClickListener {
            val name = addItemView.findViewById<EditText>(R.id.newitemname).text.toString()
            val img = addItemView.findViewById<EditText>(R.id.newitemimg).text.toString()
            val quantityText = addItemView.findViewById<EditText>(R.id.newitemcount).text.toString()
            val category = addItemView.findViewById<EditText>(R.id.newitemcategory).text.toString()
            val description = addItemView.findViewById<EditText>(R.id.newitemdescription).text.toString()

            if (quantityText.isBlank()) {
                return@setOnClickListener
            }

            val quantity = quantityText.toInt()

            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(view.context)
                val userId = LoginHelpers.getLoggedInUserId(view.context)

                db.itemTypeDao().insertItemType(
                    ItemType(
                        userId = userId,
                        name = name,
                        defaultPhotoUri = img,
                        category = category,
                        description = description
                    )
                )

                val itemTypeId = db.itemTypeDao().getItemTypeByName(name)?.id
                if (itemTypeId != null) {
                    db.inventoryItemDao().insertItem(
                        InventoryItem(
                            userId = userId,
                            itemTypeId = itemTypeId,
                            quantity = quantity
                        )
                    )
                }

                runOnUiThread {
                    adapter.updateData()
                    dialog.dismiss()
                }
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    class GridAdapter(
        private val context: Context,
        private var items: List<ItemType>,
    ) : BaseAdapter() {

        private val inflater = LayoutInflater.from(context)
        var sortBy: SortBy = SortBy.NAME_AZ
        private var isEditMode = false

        private var allItems: List<ItemType> = items

        fun setEditMode(editMode: Boolean) {
            isEditMode = editMode
            notifyDataSetChanged()
        }

        fun updateData() {
            val db = AppDatabase.getDatabase(context)
            val userId = LoginHelpers.getLoggedInUserId(context)
            val updatedList = runBlocking {
                when (sortBy) {
                    SortBy.QUANTITY_HIGH  -> db.itemTypeDao().getItemTypesSortedByQuantity(userId)
                    SortBy.QUANTITY_LOW   -> db.itemTypeDao().getItemTypesSortedByQuantity(userId).reversed()
                    SortBy.NAME_AZ        -> db.itemTypeDao().getItemTypesSortedByName(userId)
                    SortBy.NAME_ZA        -> db.itemTypeDao().getItemTypesSortedByName(userId).reversed()
                    SortBy.RECENTLY_ADDED -> db.itemTypeDao().getItemTypesSortedByRecentlyAdded(userId)
                    SortBy.OLDEST_ADDED   -> db.itemTypeDao().getItemTypesSortedByRecentlyAdded(userId).reversed()
                }
            }
            items = updatedList
            allItems = updatedList
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                notifyDataSetChanged()
            }
        }

        fun filter(query: String) {
            items = if (query.isBlank()) {
                allItems
            } else {
                allItems.filter { it.name.contains(query, ignoreCase = true) }
            }
            notifyDataSetChanged()
        }

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): Any = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(
                R.layout.item_inventory,
                parent,
                false
            )

            val image = view.findViewById<ImageView>(R.id.itemImage)
            val title = view.findViewById<TextView>(R.id.itemTitle)
            val btn4 = view.findViewById<Button>(R.id.deleteButton)

            val db = AppDatabase.getDatabase(context)
            val userId = LoginHelpers.getLoggedInUserId(context)

            val itemType = items[position]

            val count = runBlocking {
                db.itemTypeDao().getItemCount(userId, itemType.id)
            }

            Glide.with(context)
                .load(itemType.defaultPhotoUri)
                .centerCrop()
                .into(image)

            title.text = "${itemType.name}: ${count ?: 0}"

            btn4.visibility = if (isEditMode) View.VISIBLE else View.GONE

            btn4.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Delete ${itemType.name}?")
                    .setPositiveButton("Yes") { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val db = AppDatabase.getDatabase(parent.context)
                            db.itemTypeDao().deleteItemType(itemType)
                            CoroutineScope(Dispatchers.Main).launch {
                                this@GridAdapter.updateData()
                            }
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            }

            return view
        }
    }

}

