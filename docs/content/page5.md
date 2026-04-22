### Database Enhancements 

**Employ strategies for building collaborative environments that enable diverse audiences to support organizational decision making in the field of computer science:** 
The database enhancements improved collaboration by structuring data to support users, teams, and the organization. The use of normalized tables reduces redundancy and ensures consistency, allowing team members to reliably access shared data. Foreign keys and cascading rules prevent conflicts.This supports all stakeholders by providing accurate, consistent, and accessible data. 
Here is a snippet of how the get count query works for item type:
```    // get count
    @Query("""
    SELECT SUM(quantity) 
    FROM inventory_items 
    WHERE itemTypeId = :itemTypeId AND userId = :userId
    """)
```

**Design, develop, and deliver professional-quality oral, written, and visual communications that are coherent, technically sound, and appropriately adapted to specific audiences and contexts:**
The database design is very clear because of consistent naming convention and logical organization. The inclusion of fields like createdAt, updatedAt, notes, and comments supports traceability and reporting, which improves communication. 

**Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution, while managing the trade-offs involved in design choices:**
The database improvements show good problem-solving by organizing data in a way that is efficient and easy to use. For example, using queries to calculate totals (like item quantities) makes the app faster instead of doing that work in the code. I also balanced trade-offs, like keeping the database organized while making sure queries are still simple enough to run quickly. Adding indexes helps speed things up, even though it slightly increases storage use.
Here is a snippet of the query for get item sorted by quantity:
```    @Query("""
      SELECT it.* FROM item_types it
      LEFT JOIN (SELECT itemTypeId, SUM(quantity) as total
                 FROM inventory_items WHERE userId = :userId GROUP BY itemTypeId) inv
      ON it.id = inv.itemTypeId
      WHERE it.userId = :userId
      ORDER BY inv.total DESC
  """)
    suspend fun getItemTypesSortedByQuantity(userId: Int): List<ItemType>
```

**Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals:**
The database uses relationships and constraints to keep data accurate. Using Room helped connect the database to the app more easily. The design also leaves room for future features, like RFID tracking or recommendations, showing that the system can grow over time.
Here is the InventoryItem entity, with two ForeignKeys:
```@Entity(
    tableName = "inventory_items",
    foreignKeys = [
        ForeignKey( // easier queries with direct ownership instead of going through ItemType
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemType::class, // delete if user deletes item type
            parentColumns = ["id"],
            childColumns = ["itemTypeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("itemTypeId")]
)
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val itemTypeId: Int,
    val customName: String? = null,  // for individual items (ex: pink uggs)
    val quantity: Int = 1, // add one at a time to connect RFID
    val photoUri: String? = null,
    val expirationDate: Long? = null,
    val purchaseDate: Long? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(), // for ML recommendations
    val updatedAt: Long = System.currentTimeMillis()  // for ML recommendations
)
```
