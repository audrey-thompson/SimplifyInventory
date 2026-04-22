### Software Design & Engineering Enhancements

**Employ strategies for building collaborative environments that enable diverse audiences to support organizational decision making in the field of computer science:**
In this enhancement, I built out the rest of the app including all pages and UI while oraganizing the code to be maintainable and easy to understand. This would make it easy for other team members to contribute to because they wouldn't have to waste too much time figuring out where things are or how they work. 

**Design, develop, and deliver professional-quality oral, written, and visual communications that are coherent, technically sound, and appropriately adapted to specific audiences and contexts:** Documented decision-making and implementation for each step and ensured decisions were made with the user, team, and company needs in mind. Each page or feature was added because it either benefits the user (for example, the settings and accounts page) or the company (for example, the shop page). The considerations that went into each decision are well documented between the videos, code, or narratives. 

**Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution, while managing the trade-offs involved in design choices:** I understood the user, team, and business needs well before creating this project and used that understanding to deliver important features while using best practices and managing trade-offs. The features of the app are all accessible and easy for the user to work with intuitively. Elements on the screen are like the name and count of items were intentionally designed to be clear to see, with shadows surrounding the white letters for contrast. The words shown on on the inventory and settings pages are "items" and "accounts" because those could fit well in the button without the font being so small that it becomes unreadable/inaccessible.

**Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals:** The use of the menu as its own element to be added in each page rather than written multiple times was good design and helps to keep the code maintainable.
Rather than including this for each page: 
`<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:gravity="center"
    android:padding="4dp">
    <Button
        android:id="@+id/nav_home"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_margin="4dp"
        android:text="Home"
        android:background="@drawable/button_rounded"
        android:backgroundTint="@color/purple_dark"
        android:textColor="@android:color/white"/>
    <Button
        android:id="@+id/nav_inventory"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_margin="4dp"
        android:text="Items"
        android:background="@drawable/button_rounded"
        android:backgroundTint="@color/purple_dark"
        android:textColor="@android:color/white"/>
    <Button
        android:id="@+id/nav_shop"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_margin="4dp"
        android:text="Shop"
        android:background="@drawable/button_rounded"
        android:backgroundTint="@color/purple_dark"
        android:textColor="@android:color/white"/>
    <Button
        android:id="@+id/nav_settings"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_margin="4dp"
        android:text="Account"
        android:background="@drawable/button_rounded"
        android:backgroundTint="@color/purple_dark"
        android:textColor="@android:color/white"/>
</LinearLayout>
`
I only had to write that once and include this on each page: 
`    <include
        android:id="@+id/bottom_nav"
        layout="@layout/bottom_nav" />
`
