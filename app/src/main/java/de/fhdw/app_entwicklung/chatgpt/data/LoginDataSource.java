package de.fhdw.app_entwicklung.chatgpt.data;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import de.fhdw.app_entwicklung.chatgpt.MainActivity;
import de.fhdw.app_entwicklung.chatgpt.MainFragment;
import de.fhdw.app_entwicklung.chatgpt.PrefsFacade;
import de.fhdw.app_entwicklung.chatgpt.data.model.LoggedInUser;
import de.fhdw.app_entwicklung.chatgpt.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {


    public Result<LoggedInUser> login(String username, String password) {

        try {

            //String check = PreferenceManager.getDefaultSharedPreferences(context).getString("app_password", "1234");

            String check = "debug"; // no access to context

            if (password.equals(check)) {
                LoggedInUser currentUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                return new Result.Success<>(currentUser);
            } else {
                return new Result.Error(new IOException("Password Falsch"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}