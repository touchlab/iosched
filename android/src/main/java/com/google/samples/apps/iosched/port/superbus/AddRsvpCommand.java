package com.google.samples.apps.iosched.port.superbus;

import android.content.Context;
import android.util.Log;

import com.google.samples.apps.iosched.port.tasks.AddRsvpRequest;
import com.google.samples.apps.iosched.port.tasks.BasicIdResult;
import com.google.samples.apps.iosched.port.tasks.DataHelper;

import co.touchlab.android.superbus.CheckedCommand;
import co.touchlab.android.superbus.Command;
import co.touchlab.android.superbus.errorcontrol.PermanentException;
import co.touchlab.android.superbus.errorcontrol.TransientException;
import retrofit.RestAdapter;

/**
 * Created by kgalligan on 8/17/14.
 */
public class AddRsvpCommand extends CheckedCommand
{
    private Long eventId;

    public AddRsvpCommand(Long eventId)
    {
        this.eventId = eventId;
    }

    public AddRsvpCommand()
    {
    }

    @Override
    public boolean handlePermanentError(Context context, PermanentException exception) {
        //TODO: need logging strategy
        return true;
    }

    @Override
    public String logSummary() {
        return null;
    }

    @Override
    public boolean same(Command command) {
        return false;
    }

    @Override
    public void callCommand(Context context) throws TransientException, PermanentException
    {
        RestAdapter restAdapter = DataHelper.makeRequestAdapter(context);
        AddRsvpRequest addRsvpRequest = restAdapter.create(AddRsvpRequest.class);

        if(eventId != null)
        {
            BasicIdResult basicIdResult = addRsvpRequest.addRsvp(eventId);
            Log.w("asdf", "Result id: " + basicIdResult.id);
        }
        else
        {
            throw new PermanentException("Some value is null: "+ eventId);
        }
    }
}