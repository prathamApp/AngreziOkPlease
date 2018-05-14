package com.example.pravin.angreziok.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created by HP on 01-05-2018.
 */
public class RecieveFiles extends AsyncTask<Void, Integer, String> {

    String targetPath;
    String recieveProfilePath;
    //        ProgressDialog dialog;
    File newProfile;
    Context context;
    String type;

    public RecieveFiles(Context context, String targetPath, String recieveProfilePath, String type) {
        this.context = context;
        this.targetPath = targetPath;
        this.recieveProfilePath = recieveProfilePath;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//            dialog = new ProgressDialog(CrlShareReceiveProfiles.this);
//            dialog.setMessage("Receiving Profiles");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
//            dialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Extraction of contents
        if (type.equalsIgnoreCase("json")) {
            try {
//                new File(recieveProfilePath).delete();
                // Add Initial Entries of CRL & Village Json to Database
                SetInitialValuesReceiveOff();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "true";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (type.equalsIgnoreCase("profiles")) {
            EventBus.getDefault().post(new MessageEvent("showDetails"));
        }
    }


    private void copyDirectory(File source, File target) throws IOException {
        try {
            if (!target.exists()) {
                target.mkdir();
            }

            for (String f : source.list()) {
                copy(new File(source, f), new File(target, f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Copy Function to Copy file
    public static void copy(File src, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);

            // buffer size 1K
            byte[] buf = new byte[1024];

            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            is.close();
            os.close();
        }
    }

    // Reading CRL Json From internal memory
    public String loadCrlJSONFromAsset() {
        String crlJsonStr = "";

        try {
            File crlJsonSDCard = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/receivedUsage/", "Crl.json");
            if (crlJsonSDCard.exists()) {
                FileInputStream stream = new FileInputStream(crlJsonSDCard);
                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                    crlJsonStr = Charset.defaultCharset().decode(bb).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stream.close();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return crlJsonStr;
    }


    // Reading Aser Json From internal memory
    public String loadAserJSONFromAsset() {
        String aserJson = "";
        try {
            File AserJsonSDCard = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/receivedUsage/", "Aser.json");
            if (AserJsonSDCard.exists()) {
                FileInputStream stream = new FileInputStream(AserJsonSDCard);
                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                    aserJson = Charset.defaultCharset().decode(bb).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stream.close();
                }
            }

        } catch (Exception e) {
        }

        return aserJson;

    }

    // Reading Student Json From internal memory
    public String loadStudentJSONFromAsset() {
        String studentJson = "";
        try {
            File studentJsonSDCard = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/receivedUsage/", "Student.json");
            if (studentJsonSDCard.exists()) {
                FileInputStream stream = new FileInputStream(studentJsonSDCard);
                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                    studentJson = Charset.defaultCharset().decode(bb).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stream.close();
                }
            }

        } catch (Exception e) {
        }

        return studentJson;

    }

    // Reading Student Json From internal memory
    public String loadGroupJSONFromAsset() {
        String groupJson = "";
        try {
            File groupJsonSDCard = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/receivedUsage/", "Group.json");
            if (groupJsonSDCard.exists()) {
                FileInputStream stream = new FileInputStream(groupJsonSDCard);
                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                    groupJson = Charset.defaultCharset().decode(bb).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stream.close();
                }
            }
        } catch (Exception e) {
        }

        return groupJson;

    }


    void SetInitialValuesReceiveOff() throws JSONException {
/*
        // insert your code to run only when application is started first time here
        //CRL Initial DB Process
        CrlDBHelper db = new CrlDBHelper(context);
        // For Loading CRL Json From External Storage (Assets)
        JSONArray crlJsonArray = new JSONArray(loadCrlJSONFromAssetReceiveOff());
        for (int i = 0; i < crlJsonArray.length(); i++) {
            JSONObject clrJsonObject = crlJsonArray.getJSONObject(i);
            Crl crlobj = new Crl();
            crlobj.CRLId = clrJsonObject.getString("CRLId");
            crlobj.FirstName = clrJsonObject.getString("FirstName");
            crlobj.LastName = clrJsonObject.getString("LastName");
            crlobj.UserName = clrJsonObject.getString("UserName");
            crlobj.Password = clrJsonObject.getString("Password");
            crlobj.ProgramId = clrJsonObject.getInt("ProgramId");
            crlobj.Mobile = clrJsonObject.getString("Mobile");
            crlobj.State = clrJsonObject.getString("State");
            crlobj.Email = clrJsonObject.getString("Email");
            crlobj.newCrl = true;
            // new entries default values
            try {
                crlobj.sharedBy = clrJsonObject.getString("sharedBy");
                crlobj.SharedAtDateTime = clrJsonObject.getString("SharedAtDateTime");
                crlobj.appVersion = clrJsonObject.getString("appVersion");
                crlobj.appName = clrJsonObject.getString("appName");
                crlobj.CreatedOn = clrJsonObject.getString("CreatedOn");
            } catch (Exception e) {
                crlobj.sharedBy = "";
                crlobj.SharedAtDateTime = "";
                crlobj.appVersion = "";
                crlobj.appName = "";
                crlobj.CreatedOn = "";
                e.printStackTrace();
            }
            db.updateJsonData(crlobj);
            BackupDatabase.backup(context);
        }
        //Villages Initial DB Process
        VillageDBHelper database = new VillageDBHelper(context);
        // For Loading Villages Json From External Storage (Assets)
        JSONArray villagesJsonArray = new JSONArray(loadVillageJSONFromAssetReceiveOff());
        for (int j = 0; j < villagesJsonArray.length(); j++) {
            JSONObject villagesJsonObject = villagesJsonArray.getJSONObject(j);
            Village villageobj = new Village();
            villageobj.VillageID = villagesJsonObject.getInt("VillageId");
            villageobj.VillageCode = villagesJsonObject.getString("VillageCode");
            villageobj.VillageName = villagesJsonObject.getString("VillageName");
            villageobj.Block = villagesJsonObject.getString("Block");
            villageobj.District = villagesJsonObject.getString("District");
            villageobj.State = villagesJsonObject.getString("State");
            villageobj.CRLID = villagesJsonObject.getString("CRLId");
            database.updateJsonData(villageobj);
            BackupDatabase.backup(context);
        }
*/
    }

    // Reading CRL Json From Internal Memory
    public String loadCrlJSONFromAssetReceiveOff() {
        String crlJsonStr = null;
        try {
            File crlJsonSDCard = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Json/", "Crl.json");
            FileInputStream stream = new FileInputStream(crlJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                crlJsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return crlJsonStr;
    }

    // Reading Village Json From SDCard
    public String loadVillageJSONFromAssetReceiveOff() {
        String villageJson = null;
        try {
            File villageJsonSDCard = new File(Environment.getExternalStorageDirectory() + "/.POSinternal/Json/", "Village.json");
            FileInputStream stream = new FileInputStream(villageJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                villageJson = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return villageJson;

    }

}
