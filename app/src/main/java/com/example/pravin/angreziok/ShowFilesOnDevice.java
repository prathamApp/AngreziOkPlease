package com.example.pravin.angreziok;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.pravin.angreziok.interfaces.FolderClick;
import com.example.pravin.angreziok.modalclasses.Modal_DownloadContent;
import com.example.pravin.angreziok.util.SDCardUtil;
import com.google.gson.Gson;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShowFilesOnDevice extends AppCompatActivity implements FolderClick {

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private static final String TABLE_PARENT = "table_parent_content";
    private static final String TABLE_CHILD = "table_child_content";
    DocumentFile parenturi;
    DocumentFile tempUri;
    ProgressDialog pd;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<File_Model> data = new ArrayList<File_Model>();
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems = new ArrayList<Integer>();
    FTPClient client1;
    String treeUri;
    ArrayList<Integer> level = new ArrayList<>();
    private File tempFile;
    long totalDirSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_files_on_device);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewExplorer);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        treeUri = PreferenceManager.getDefaultSharedPreferences(ShowFilesOnDevice.this).getString("URI", "");
        client1 = AOPApplication.ftpClient;

        if (!client1.equals(null)) {
            listFtpFiles(false, null, false);
        }
    }

    private void listFtpFiles(boolean isExpand, String name, boolean changeToParent) {
        new AsyncTask<Void, Void, FTPFile[]>() {
            @Override
            protected FTPFile[] doInBackground(Void... voids) {
                // todo get FTP Server's file's paths & details
                try {
                    if (isExpand) {
                        level.add(1);
                        client1.changeWorkingDirectory(name);
                    }
                    if (changeToParent) {
                        level.remove(level.size() - 1);
                        client1.changeToParentDirectory();
                    }
                    FTPFile[] files = client1.listFiles();
                    return files;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(FTPFile[] files) {
                super.onPostExecute(files);
                data.clear();
                int i=0;
                for (FTPFile mfile : files) {
                    File_Model model = new File_Model();
                    model.setMfile(mfile);
                    model.setFileName(mfile.getName());
                    model.setFile(mfile.isFile());
                    data.add(model);
                }
                Log.d("data_size::", data.size() + "");
                adapter = new File_Adapter(data, ShowFilesOnDevice.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        if (level.size() > 0) {
            listFtpFiles(false, null, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDownload(int position, FTPFile name) {
        File final_file = null;
        DocumentFile finalDocumentFile = null;
        boolean isSdCard = PreferenceManager.getDefaultSharedPreferences(ShowFilesOnDevice.this).getBoolean("IS_SDCARD", false);
        if (!isSdCard) {
            String path = PreferenceManager.getDefaultSharedPreferences(ShowFilesOnDevice.this).getString("PATH", "");
            if (name.getName().equalsIgnoreCase(".AOP_External")) {
                File file = new File(path, "/");
                if (!file.exists())
                    file.mkdir();
                final_file = file;
            } else {
                File file = new File(path, "");
                if (!file.exists())
                    file.mkdir();
                File child_file = new File(file, name.getName());
                if (!child_file.exists())
                    child_file.mkdir();
                final_file = child_file;
            }
        } else {
//            totalDirSize = name.getSize();

            DocumentFile documentFile = DocumentFile.fromTreeUri(ShowFilesOnDevice.this, Uri.parse(treeUri));
            Log.d("GetName", "onDownload: " + name.getName());
            //check whether root folder ".AOP_External" exists or not
            if (documentFile.findFile(".AOP_External") == null) {
                documentFile = documentFile.createDirectory(".AOP_External");
            } else {
                documentFile = documentFile.findFile(".AOP_External");
            }

            DocumentFile documentFile1 = documentFile.findFile(name.getName());
            if (documentFile1 == null)
                documentFile = documentFile.createDirectory(name.getName());
            else {
                documentFile = documentFile1;
                Log.d("totalDirSize", "onDownload: "+totalDirSize);
            }
            finalDocumentFile = documentFile;
        }
        DocumentFile finalDocumentFile1 = finalDocumentFile;
        File final_file1 = final_file;
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    client1.changeWorkingDirectory(".AOP_External");
/*
                    final long totalProgressTime = name.;
                    final Thread t = new Thread() {
                        @Override
                        public void run() {
                            int jumpTime = 0;

                            while (jumpTime < totalProgressTime) {
                                try {
                                    sleep(200);
                                    if(name.isFile())
                                        jumpTime += name.getSize();
                                    pd.setProgress(jumpTime);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();*/


                    if (name.isDirectory()) {
                        if (isSdCard) {
                            downloadDirectoryToSdCard(client1, finalDocumentFile1, name);
                        }else
                            downloadDirectoryToInternal(client1, final_file1, name);
                    } else {
                        if (isSdCard)
                            downloadFile(client1, name, finalDocumentFile1);
                        else
                            downloadFile(client1, name, finalDocumentFile1);
                    }
//                    publishProgress((int) name.getSize());
//                    client1.changeToParentDirectory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                pd.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (pd != null)
                    pd.dismiss();
            }
        }.execute();

    }

    public  void showProgressDialog(){
        pd = new ProgressDialog(ShowFilesOnDevice.this);
        pd.setMessage("Please Wait !!! Downloading ...");
        pd.setCanceledOnTouchOutside(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setIndeterminate(true);
        pd.setMax(100);
//        pd.setProgress(0);
        pd.show();
    }


    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size=f.length();
        }
        return size;
    }

    private void downloadDirectoryToInternal(FTPClient client1, File final_file1, FTPFile name) {
        try {
            tempFile = final_file1;
            FTPClient tempClient = client1;
            if (name != null)
                tempClient.changeWorkingDirectory(name.getName());
            FTPFile[] subFiles = tempClient.listFiles();
            Log.d("file_sizeInternal::", subFiles.length + "");
            if (subFiles != null && subFiles.length > 0) {
                for (FTPFile aFile : subFiles) {
                    Log.d("nameInternal::", AOPApplication.getSdCardPath() + aFile.getName() + "");
                    String currentFileName = AOPApplication.getSdCardPath() + aFile.getName();
                    if (currentFileName.equals(".") || currentFileName.equals("..")) {
                        // skip parent directory and the directory itself
                        continue;
                    }
                    if (aFile.isDirectory()) {
                        // create the directory in saveDir
                        File file = new File(tempFile.getAbsolutePath(), currentFileName);
                        if (!file.exists())
                            file.mkdir();
                        tempClient.changeWorkingDirectory(currentFileName);
                        downloadDirectoryToInternal(client1, file, null);
                    } else {
                        downloadFileToInternal(client1, aFile, tempFile);
                    }
                }
                tempFile = tempFile.getParentFile();
                tempClient.changeToParentDirectory();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFileToInternal(FTPClient client1, FTPFile aFile, File tempFile) {
        try {
            tempFile = new File(AOPApplication.getSdCardPath() + tempFile, aFile.getName());
            Log.d("tempFileInternal::", tempFile.getAbsolutePath());
            OutputStream outputStream = ShowFilesOnDevice.this.getContentResolver().openOutputStream(Uri.fromFile(tempFile));
            client1.setFileType(FTP.BINARY_FILE_TYPE);
            client1.retrieveFile(aFile.getName(), outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Todo copy json to database
            if (aFile.getName().endsWith(".json")) {
                Log.d("json_path:::", tempFile.getAbsolutePath() + "");
                Log.d("json_path:::", aFile.getName() + "");
                //Todo read json from file
                String response = loadJSONFromAsset(tempFile.getAbsolutePath());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Modal_DownloadContent download_content = gson.fromJson(jsonObject.toString(), Modal_DownloadContent.class);
            }
        }
    }

    @Override
    public void onFolderClicked(int position, String name) {
        listFtpFiles(true, name, false);
    }

    String json;

    // todo put in async task downdir and file
    private void downloadDirectoryToSdCard(FTPClient ftpClient, DocumentFile documentFile, FTPFile name) {
        try {
            tempUri = documentFile;
            FTPClient tempClient = ftpClient;
            if (name != null)
                tempClient.changeWorkingDirectory(name.getName());
            FTPFile[] subFiles = tempClient.listFiles();
            Log.d("file_sizeSdCard::", subFiles.length + "");
            if (subFiles != null && subFiles.length > 0) {
                for (FTPFile aFile : subFiles) {
                    Log.d("nameSdCard::", aFile.getName() + "");
                    String currentFileName = aFile.getName();
                    if (currentFileName.equals(".") || currentFileName.equals("..")) {
                        // skip parent directory and the directory itself
                        continue;
                    }
                    if (aFile.isDirectory()) {
                        // create the directory in saveDir
                        if (tempUri.findFile(currentFileName) == null) {
                            tempUri = tempUri.createDirectory(currentFileName);
                        }
                        tempClient.changeWorkingDirectory(currentFileName);
                        downloadDirectoryToSdCard(ftpClient, tempUri, null);
                    } else {
                        downloadFile(ftpClient, aFile, tempUri);
                        Log.d("downloadDirectSdCard:", (Math.round((aFile.getSize() / (1024* 1024)) * 10)+"")) ;
                        pd.setProgress((int) (Math.round((aFile.getSize() / (1024* 1024)) * 10) / 10)*100/subFiles.length);
                    }
                }
                tempUri = tempUri.getParentFile();
                tempClient.changeToParentDirectory();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(FTPClient ftpClient, FTPFile ftpFile, DocumentFile tempFile) {
        try {
            if (tempFile.findFile(ftpFile.getName()) == null)
                tempFile = tempFile.createFile("image", ftpFile.getName());
            else
                tempFile = tempFile.findFile(ftpFile.getName());
            OutputStream outputStream = ShowFilesOnDevice.this.getContentResolver().openOutputStream(tempFile.getUri());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(ftpFile.getName(), outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Todo copy json to database
//            if (ftpFile.getName().endsWith(".json")) {
//                String path = SDCardUtil.getRealPathFromURI_API19(ShowFilesOnDevice.this, tempFile.getUri());
//                Log.d("json_path:::", path + "");
//                Log.d("json_path:::", ftpFile.getName() + "");
//                //Todo read json from file
//                String response = loadJSONFromAsset(path);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Gson gson = new Gson();
//                Modal_DownloadContent download_content = gson.fromJson(jsonObject.toString(), Modal_DownloadContent.class);
//            }
        }
    }

    // Reading Json From Internal Storage
    public String loadJSONFromAsset(String path) {
        String JsonStr = null;

        try {
            File queJsonSDCard = new File(path);
            FileInputStream stream = new FileInputStream(queJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                JsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return JsonStr;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

}