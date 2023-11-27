package de.fhdw.app_entwicklung.chatgpt.data;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import de.fhdw.app_entwicklung.chatgpt.PrefsFacade;
import de.fhdw.app_entwicklung.chatgpt.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            /*LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randoamUUID().toString(),
                            username);
            return new Result.Success<>(fakeUser);*/
            // TODO: handle loggedInUser authentication

            String check = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("app_password", "1234");

            if (password.equals("1234") || password.equals(check)) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                return new Result.Success<>(fakeUser);
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