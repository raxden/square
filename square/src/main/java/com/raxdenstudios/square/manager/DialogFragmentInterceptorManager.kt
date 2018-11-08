package com.raxdenstudios.square.manager

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raxdenstudios.square.interceptor.DialogFragmentInterceptor
import com.raxdenstudios.square.lifecycle.DialogFragmentLifecycle

/**
 * Created by Ángel Gómez on 18/12/2016.
 */

class DialogFragmentInterceptorManager(fragment: DialogFragment) : InterceptorManager<DialogFragment, DialogFragmentInterceptor<*>>(fragment), DialogFragmentLifecycle {

    override fun onSaveInstanceState(outState: Bundle) {
        interceptors.forEach { it.onSaveInstanceState(outState) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle) {
        interceptors.forEach { it.onViewStateRestored(savedInstanceState) }
    }

    override fun onAttach(activity: Activity?) {
        interceptors.forEach { it.onAttach(activity) }
    }

    override fun onAttach(context: Context?) {
        interceptors.forEach { it.onAttach(context) }
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        interceptors.forEach { it.onAttachFragment(childFragment) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        interceptors.forEach { it.onCreate(savedInstanceState) }
    }

    override fun onCreateView(inflater: LayoutInflater, inflatedView: View?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var inflatedView = inflatedView
        interceptors.forEach { inflatedView = it.onCreateView(inflater, inflatedView, container, savedInstanceState) }
        return inflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        interceptors.forEach { it.onViewCreated(view, savedInstanceState) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        interceptors.forEach { it.onActivityCreated(savedInstanceState) }
    }

    override fun onStart() {
        interceptors.forEach { it.onStart() }
    }

    override fun onResume() {
        interceptors.forEach { it.onResume() }
    }

    override fun onPause() {
        interceptors.forEach { it.onPause() }
    }

    override fun onStop() {
        interceptors.forEach { it.onStop() }
    }

    override fun onDestroy() {
        interceptors.forEach { it.onDestroy() }
    }

    override fun onDestroyView() {
        interceptors.forEach { it.onDestroyView() }
    }

    override fun onDetach() {
        interceptors.forEach { it.onDetach() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        interceptors.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    override fun onConfigurationChanged(configuration: Configuration?) {
        interceptors.forEach { it.onConfigurationChanged(configuration) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?, dialog: Dialog): Dialog {
        var dialog = dialog
        interceptors.forEach { dialog = it.onCreateDialog(savedInstanceState, dialog) }
        return dialog
    }

    override fun onCancel(dialog: DialogInterface?) {
        interceptors.forEach { it.onCancel(dialog) }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        interceptors.forEach { it.onDismiss(dialog) }
    }

}