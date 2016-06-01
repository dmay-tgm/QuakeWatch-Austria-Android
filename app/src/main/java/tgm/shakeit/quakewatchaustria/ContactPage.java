package tgm.shakeit.quakewatchaustria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Activity that displays input fields for comments and contact.
 *
 * @author Daniel May
 * @version 2016-05-31
 */
public class ContactPage extends AppCompatActivity {
    private TextInputEditText comment, contact;

    /**
     * Inflates the layout.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_and_contact);

        contact = (TextInputEditText) findViewById(R.id.input_contact);
        comment = (TextInputEditText) findViewById(R.id.input_comment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                /**
                 * Sets the JSON values, that have been entered.
                 * @param v the view that was clicked on
                 */
                @Override
                public void onClick(View v) {
                    String comment_text = comment.getText().toString().trim();
                    String contact_text = contact.getText().toString().trim();
                    if (!comment_text.isEmpty())
                        Report.setComment(comment_text);
                    if (!contact_text.isEmpty())
                        Report.setContact(contact_text);
                    new SendOperation().execute();
                }
            });
        }
    }

    /**
     * Sends the earthquake report.
     */
    private class SendOperation extends AsyncTask<String, String, String> {

        private ProgressDialog mDialog;
        private JSONSender js;

        /**
         * Gets called before executing
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(ContactPage.this);
            mDialog.setMessage("Erdbebenbericht senden...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        /**
         * Background execution
         *
         * @param params parameters
         * @return a String
         */
        @Override
        protected String doInBackground(String... params) {
            js = new JSONSender(getBaseContext());
            js.addToQueue(Report.toJSON().toString(), getBaseContext());
            js.sendQueued(getBaseContext());
            return null;
        }

        /**
         * Gets called after the background activity was executed
         *
         * @param strFromDoInBg string
         */
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            Toast.makeText(getBaseContext(), "Ihre Meldung wird verarbeitet.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ContactPage.this, NaviDrawer.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}