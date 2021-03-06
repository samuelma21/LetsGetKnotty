package edu.rosehulman.samuelma.letsgetknotty.createPattern

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.samuelma.letsgetknotty.Constants
import edu.rosehulman.samuelma.letsgetknotty.R
import edu.rosehulman.samuelma.letsgetknotty.pattern.Pattern
import edu.rosehulman.samuelma.letsgetknotty.project.Project


private const val ARG_PATTERN = "pattern"
private const val ARG_PROJECT = "project"
private const val ARG_UID = "uid"

class CreatePatternFragment: Fragment() {
    private lateinit var pattern : Pattern
    private lateinit var project: Project
    private var uid: String = ""
    private lateinit var adapter: CreatePatternAdapter
    companion object {
        @JvmStatic
        fun newInstance(uid :String, pattern: Pattern, project : Project) =
            CreatePatternFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PATTERN, pattern)
                    putParcelable(ARG_PROJECT, project)
                    putString(ARG_UID, uid)
                }
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pattern = arguments!!.getParcelable(ARG_PATTERN)!!
        project = arguments!!.getParcelable(ARG_PROJECT)!!
        uid = arguments!!.getString(ARG_UID).toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_pattern, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.create_pattern_grid_view)
        adapter = context?.let { CreatePatternAdapter(it,uid,project,pattern) }!!
        recyclerView.layoutManager =
            GridLayoutManager(context,pattern.stitchesInRepeat)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        adapter.addSnapshotListener()
        val button : Button = view.findViewById(R.id.choose_color_button)
        button.setOnClickListener {
            showColorDialog(button)
        }

        view.findViewById<TextView>(R.id.pattern_name_textview).text = pattern.name

        //add button
        val addPattern = view.findViewById<TextView>(R.id.add_create_pattern)
        addPattern.setOnClickListener {
            onAddSelected()
        }

        //cancel button
        val cancelCreatePattern = view.findViewById<TextView>(R.id.cancel_create_pattern)
        cancelCreatePattern.setOnClickListener {
            onCancelSelected()
        }

        createGrid()
        return view
    }

    private fun createGrid() {
        for(x in 0 until pattern.stitchesInRepeat*pattern.rowsInRepeat) {
            adapter.add(Grid())
        }
    }

    // From https://android-arsenal.com/details/1/1693
    private fun showColorDialog(colorButton : Button) {
        val builder = ColorPickerDialogBuilder.with(context)
        builder.setTitle("Choose HSV color")
        builder.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
        builder.density(12)
        builder.setOnColorSelectedListener { selectedColor ->
//            Toast.makeText(
////                context,
////                "onColorSelected: 0x" + Integer.toHexString(selectedColor),
////                Toast.LENGTH_SHORT
////            ).show()
        }
        builder.setPositiveButton(android.R.string.ok) { dialog, selectedColor, allColors ->
            //  colorMessage.message = activity_input_message.text.toString()
            //val color = selectedColor
            colorButton.setBackgroundColor(selectedColor)
            adapter.color = selectedColor
        }
        builder.setNegativeButton(getString(android.R.string.cancel), null)
        builder.build().show()
    }

    fun onAddSelected() {
        val fm = fragmentManager
        Log.d(Constants.TAG, "Trying to close create pattern fragment")
        fm?.popBackStack()
    }

    fun onCancelSelected() {
        //probably a better way to do this
        FirebaseFirestore
            .getInstance()
            .collection(Constants.USERS_COLLECTION)
            .document(uid)
            .collection(Constants.PROJECTS_COLLECTION)
            .document(project.id)
            .collection(Constants.PATTERNS_COLLECTION)
            .document(pattern.id).delete()

        val fm = fragmentManager
        Log.d(Constants.TAG, "Trying to cancel create pattern fragment B")
        fm?.popBackStack()
    }

}