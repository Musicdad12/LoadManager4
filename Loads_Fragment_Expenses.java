package com.jrschugel.loadmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Loads_Fragment_Expenses extends Fragment  {

    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    DatabaseHelperExpense myDb3;
    Integer LoadNumber;
    SessionManager session;
    private Uri pictureUri;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "Loads_Frag_4";
    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub__fragment04, container, false);

        context = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            LoadNumber = Integer.parseInt(b.getString("LoadNumber"));
        }

        final Spinner spinDesc = rootView.findViewById(R.id.spinnerDescription);
        final Spinner spinType = rootView.findViewById(R.id.spinnerPaymentType);
        final EditText etPONum = rootView.findViewById(R.id.textViewPONumber);
        final EditText etGallon = rootView.findViewById(R.id.textViewGallons);
        final EditText etAmount = rootView.findViewById(R.id.textViewAmount);

        Button SaveButton = rootView.findViewById(R.id.butSave);
        Button PrintButton = rootView.findViewById(R.id.butPrint);
        Button ScanButton = rootView.findViewById(R.id.butScan);
        Button SendButton = rootView.findViewById(R.id.butSend);

        final TextView tvLoadNumber = rootView.findViewById(R.id.textViewLoadNumber);
        tvLoadNumber.setText(String.valueOf(LoadNumber));
        myDb3 = new DatabaseHelperExpense(getContext());

        JSONArray jsonArray = myDb3.getExpenseDataJSON(getContext(), LoadNumber);

        final ListView LoadsList = rootView.findViewById(R.id.lstExpenses);

        JSONAdapterLoadsExpense jSONAdapter = new JSONAdapterLoadsExpense (getActivity(), jsonArray);//jArray is your json array

        //Set the above adapter as the adapter of choice for our list
        LoadsList.setAdapter(jSONAdapter);

        PrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDocument();
            }
        });

        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureUri = getOutputMediaFileUri();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);

            }
        });

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveTripSheet(v);

                Uri Filename, TripSheet = null;
                ArrayList<Uri> uris = new ArrayList<>();

                myDb = new DatabaseHelper(context);
                Cursor curScans = myDb.getScanURI(LoadNumber);
                curScans.moveToFirst();
                while (!curScans.isAfterLast()) {
                    if (curScans.getString(3).contains("TripSheet")) {
                        TripSheet = Uri.parse(curScans.getString(3));
                    } else {
                        Filename = Uri.parse(curScans.getString(3));
                        uris.add(Filename);
                        Log.d(TAG, "onClick: retrieved " + Filename.toString());
                    }
                    curScans.moveToNext();
                }
                curScans.close();
                uris.add(0, TripSheet);
                final Intent ei = new Intent(Intent.ACTION_SEND_MULTIPLE);
                ei.setType("plain/text");
                ei.putExtra(Intent.EXTRA_EMAIL, new String[] {"seanmccallister12@gmail.com"});
                ei.putExtra(Intent.EXTRA_SUBJECT, LoadNumber + " Trip scan");
                ei.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                ei.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                String msgStr = "Share...";
                startActivity(Intent.createChooser(ei, msgStr));

            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String Description = spinDesc.getSelectedItem().toString();
                final String Type = spinType.getSelectedItem().toString();
                final String PONumber = etPONum.getText().toString();
                final String Gallons = etGallon.getText().toString();
                final String Amount = etAmount.getText().toString();
                final String mySQLDesc = DescriptionCode(Description);
                final String mySQLType = PaymentTypeCode(Type);

                myDb3.AddExpense(LoadNumber, Description, Type, PONumber, Gallons, Amount);
                // Refresh the listview
                LoadsList.invalidate();
                JSONArray jsonArray = myDb3.getExpenseDataJSON(getContext(), LoadNumber);
                JSONAdapterLoadsExpense jSONAdapter = new JSONAdapterLoadsExpense (getActivity(), jsonArray);//jArray is your json array
                LoadsList.setAdapter(jSONAdapter);
                etPONum.setText(null);
                etGallon.setText(null);
                etAmount.setText(null);

                //send info to online mysql database
                RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
                String url = "http://truckersean.com/android/ExpensesAddfromAndroid.php";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("LoadNumber", LoadNumber.toString());
                        params.put("Description", mySQLDesc);
                        params.put("Method", mySQLType);
                        params.put("PONumber", PONumber);
                        params.put("Gallons", Gallons);
                        params.put("Total", Amount);

                        return params;
                    }
                };
                queue.add(postRequest);
            }
        });

        LoadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View v,
                                    int position, long id) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Dialog", "You clicked OK");
                                TextView tvLoadNumber = v.findViewById(R.id.textViewLoadNumber);
                                TextView tvDescription = v.findViewById(R.id.textViewDescription);
                                TextView tvPaymentType = v.findViewById(R.id.textViewPaymentType);
                                TextView tvPONumber = v.findViewById(R.id.textViewPONumber);
                                TextView tvGallons = v.findViewById(R.id.textViewGallons);
                                TextView tvAmount = v.findViewById(R.id.textViewAmount);
                                final Integer LoadNumber = Integer.parseInt(tvLoadNumber.getText().toString());
                                final String Description = tvDescription.getText().toString();
                                final String PaymentType = tvPaymentType.getText().toString();
                                String PONumber = tvPONumber.getText().toString();
                                String Gallons = tvGallons.getText().toString();
                                String Amount = tvAmount.getText().toString();
                                Boolean Success = myDb3.DeleteExpensefromListview(LoadNumber, Description, PaymentType, PONumber, Gallons, Amount);
                                if (Success) {
                                    Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Delete Unsuccessful", Toast.LENGTH_LONG).show();
                                }
                                LoadsList.invalidate();
                                JSONArray jsonArray = myDb3.getExpenseDataJSON(getContext(), LoadNumber);
                                JSONAdapterLoadsExpense jSONAdapter = new JSONAdapterLoadsExpense (getActivity(), jsonArray);//jArray is your json array
                                LoadsList.setAdapter(jSONAdapter);
                                // Delete from online Database
                                if (PONumber.equals("")) {
                                    PONumber = "0" ;
                                }
                                if (Gallons.equals("")) {
                                    Gallons = "0";
                                }
                                if (Amount.equals("")) {
                                    Amount = "0";
                                }
                                final String finalPONumber = PONumber;
                                final String finalGallons = Gallons;
                                final String finalAmount = Amount;

                                RequestQueue requestQueueStops = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

                                StringRequest stringRequestStops = new StringRequest(Request.Method.POST, Config.DATA_DELETE_EXPENSE,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String StopsResponse) {
                                                Toast.makeText(getContext(), "Online Record Deleted", Toast.LENGTH_LONG).show();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("Volley Error: ", error.toString());
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> parameters = new HashMap<>();
                                        parameters.put("LoadNumber", LoadNumber.toString());
                                        parameters.put("Description", DescriptionCode(Description));
                                        parameters.put("PaymentType", PaymentTypeCode(PaymentType));
                                        parameters.put("PONumber", finalPONumber);
                                        parameters.put("Gallons", finalGallons);
                                        parameters.put("Amount", finalAmount);

                                        return parameters;
                                    }
                                };
                                //Adding request to the queue
                                requestQueueStops.add(stringRequestStops);

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Dialog", "You clicked Cancel");
                                String test = tvLoadNumber.getText().toString();
                                Log.i("LoadNumber", test);
                            }
                        })
                        .show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Did the user choose OK?  If so, the code inside these curly braces will execute.
        if (resultCode == RESULT_OK) {
            if (requestCode == MY_CAMERA_REQUEST_CODE) {
                myDb = new DatabaseHelper(context);
                myDb.putScanURI(LoadNumber, "Scan Image", pictureUri.toString());
                Log.d(TAG, "onClick: " + pictureUri.toString());
            }
        }
    }

    private Uri getOutputMediaFileUri()
    {
        //check for external storage
        if(isExternalStorageAvaiable())
        {
            File mediaStorageDir = Environment.getExternalStorageDirectory();
            File Directory = new File(mediaStorageDir, "LoadImages");
            if (!Directory.exists()) {
                if (Directory.mkdirs()) {
                    Log.d(TAG, "getOutputMediaFileUri: " + Directory + "created");
                }
            }
            String fileName, fileType;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

            fileName = LoadNumber + "_" + timeStamp;
            fileType = ".jpg";

            File mediaFile;
            try
            {
                mediaFile = File.createTempFile(fileName, fileType, Directory);
                Log.i("st"," Created File: " + Uri.fromFile(mediaFile));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.i("St","Error creating file: " + Directory.getAbsolutePath() +fileName +fileType);
                return null;
            }
            String authority = BuildConfig.APPLICATION_ID + ".provider";
            return FileProvider.getUriForFile(context, authority, mediaFile);
        }
        //something went wrong
        return null;
    }

        private boolean isExternalStorageAvaiable() {
        String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
    }

    public String getShownLoad() {
        // Returns the index assigned
        return LoadsListAdapter.mCurrentLoadNumber;
    }

    private String DescriptionCode (String Description) {
        switch (Description) {
            case "Tractor":
                return "1";
            case "Reefer":
                return "2";
            case "DEF":
                return "3";
            case "Scale":
                return "4";
            case "Lumper":
                return "5";
            case "Washout":
                return "6";
            case "Tolls":
                return "11";
            case "Repair":
                return "12";
            case "Late Fee":
                return "13";
            case "Pallets":
                return "14";
            case "Misc. Expenses":
                return "17";
        }
        return "0";
    }

    private String PaymentTypeCode (String PaymentType) {
        switch (PaymentType) {
            case "Fuel Card":
                return "FC";
            case "Cash/Comchek":
                return "CA";
            case "Open Charge":
                return "OC";
            case "Terminal Fuel":
                return "TF";
        }
        return "0";
    }

    public class MyPrintDocumentAdapter extends PrintDocumentAdapter
    {
        Context context;
        private int pageHeight;
        private int pageWidth;
        private PdfDocument myPdfDocument;
        private int totalpages = 1;

        private MyPrintDocumentAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle metadata) {
            myPdfDocument = new PrintedPdfDocument(context, newAttributes);

            pageHeight = Objects.requireNonNull(newAttributes.getMediaSize()).getHeightMils() / 1000 * 72;
            pageWidth = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            if (totalpages > 0) {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder(LoadNumber.toString() )
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalpages);

                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }
        private void drawPage(PdfDocument.Page page,
                              int pagenumber) {

            Log.d(TAG, "drawPage: " + pagenumber);
            session = new SessionManager(context);
            HashMap<String, String> user = session.getUserDetails();
            //Get trip variables from session
            String USERCODE = user.get(SessionManager.KEY_USERNAME);
            String TRUCK_NO = user.get(SessionManager.KEY_TRUCK);
            Integer LOAD_NO = LoadNumber;
            String NAME = user.get(SessionManager.KEY_NAME);
            //get remaining trip variables
            myDb = new DatabaseHelper(getContext());
            myDb2 = new DatabaseHelperStops(getContext());

            Cursor cursor =  myDb.getLoadData(LoadNumber);
            String TRAILER = "_", BOLNUMBER = "_", PIECES = "??", WEIGHT = "??", SHIPDATEshort = "???";
            if (cursor.moveToFirst()) {
                TRAILER = cursor.getString(cursor.getColumnIndex(Config.TAG_TRLRNUMBER));
                BOLNUMBER = cursor.getString(cursor.getColumnIndex(Config.TAG_BOLNUMBER));
                PIECES = cursor.getString(cursor.getColumnIndex(Config.TAG_PIECES));
                WEIGHT = cursor.getString(cursor.getColumnIndex(Config.TAG_WEIGHT));

            }

            myDb2 = new DatabaseHelperStops(getContext());
            Cursor cursorShipper = myDb2.getFirstShipperData(LoadNumber);
            String SHIPDATE = "??", SHIPNAME = "??", SHIPCITYST = "??";
            String CONSNAME = "??", CONSCITYST = "??";
            String EXPENSE, TYPE, PONUMBER, GALLONS, TOTAL;
            if (cursorShipper.moveToFirst()) {
                SHIPDATE = cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPEARLYDATE));
                SHIPNAME = cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPCUSTNAME));
                SHIPCITYST = cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPCUSTCITY)) + ", " +
                                    cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPCUSTSTATE));}
            cursorShipper.close();
            Cursor cursorConsignee = myDb2.getLastConsigneeData(LoadNumber);
            if (cursorConsignee.moveToFirst()) {
                CONSNAME = cursorConsignee.getString(cursorConsignee.getColumnIndex(Config.TAG_STOPCUSTNAME));
                CONSCITYST = cursorConsignee.getString(cursorConsignee.getColumnIndex(Config.TAG_STOPCUSTCITY)) + ", " +
                                    cursorConsignee.getString(cursorConsignee.getColumnIndex(Config.TAG_STOPCUSTSTATE));}
            cursorConsignee.close();

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                Date dateValue = input.parse(SHIPDATE);
                SHIPDATEshort = output.format(dateValue);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Canvas canvas = page.getCanvas();

            int titleBaseLine = 100;

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            try {
                Drawable d;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    d = getResources().getDrawable(R.drawable.blank_trip_sheet, context.getTheme());
                } else {
                    d = ContextCompat.getDrawable(context, R.drawable.blank_trip_sheet);
                }
                d.setBounds(25, 25, 575, 775);
                d.draw(canvas);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

            paint.setTextSize(14);
            canvas.drawText(NAME, 200, titleBaseLine, paint);

            canvas.drawText(SHIPDATEshort, 395, titleBaseLine, paint);
            canvas.drawText(TRAILER, 475, titleBaseLine, paint);

            Integer dataBaseLine = 150;
            paint.setTextSize(14);
            char character;
            int index = 0;
            for (int i = (6 - USERCODE.length()); i < 6 ; i++) {
                character = USERCODE.charAt(index);
                canvas.drawText(Character.toString(character), 59 + (i * 14), dataBaseLine, paint);
                canvas.drawCircle(64 + (i * 14), 164 + ((character - 65) * 14), 5, paint);
                index++;
            }
            index = 0;
            for (int i = (6 - TRUCK_NO.length()); i < 6 ; i++) {
                character = TRUCK_NO.charAt(index);
                canvas.drawText(Character.toString(character), 165 + (i * 14), dataBaseLine, paint);
                canvas.drawCircle(169 + (i * 14), 164 + ((character - 48) * 14), 5, paint);
                index++;
            }
            index = 0;
            for (int i = (7 - LOAD_NO.toString().length()); i < 7 ; i++) {
                character = LOAD_NO.toString().charAt(index);
                canvas.drawText(Character.toString(character), 269 + (i * 14), dataBaseLine, paint);
                canvas.drawCircle(271 + (i * 14), 164 + ((character - 48) * 14), 5, paint);
                index++;
            }
            paint.setTextSize(10);
            canvas.drawText(SHIPNAME, 190, 370, paint);
            canvas.drawText(SHIPCITYST, 190, 383, paint);
            canvas.drawText(CONSNAME, 375, 370, paint);
            canvas.drawText(CONSCITYST, 375, 383, paint);
            canvas.drawText(BOLNUMBER, 180, 396, paint);
            canvas.drawText(PIECES, 410, 396, paint);
            canvas.drawText(WEIGHT, 485, 396, paint);

            //Place Expenses
            paint.setTextSize(8);
            Cursor cursorExpenses = myDb3.getExpenseData(LoadNumber);
            if (cursorExpenses != null) {
                index = 1;
                Integer rowBase = 563;
                Integer colBase = 160;
                Integer horSpacing = 13;
                cursorExpenses.moveToFirst();
                do {
                        EXPENSE = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPDESC));
                        TYPE = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPTYPE));
                        PONUMBER = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPPONUM));
                        GALLONS = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPGALLON));
                        TOTAL = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPAMOUNT));
                    if (index < 8) {
                        canvas.drawText(EXPENSE, colBase, rowBase + (horSpacing * index), paint);
                        canvas.drawText(PaymentTypeCode(TYPE), colBase + 65, rowBase + (horSpacing * index), paint);
                        canvas.drawText(PONUMBER, colBase + 80, rowBase + (horSpacing * index), paint);
                        canvas.drawText(roundOneDecimal(GALLONS), colBase + 115, rowBase + (horSpacing * index), paint);
                        canvas.drawText(TOTAL, colBase + 135, rowBase + (horSpacing * index), paint);
                    } else {
                        colBase = 330;
                        canvas.drawText(EXPENSE, colBase, rowBase + (horSpacing * (index - 7)), paint);
                        canvas.drawText(PaymentTypeCode(TYPE), colBase + 65, rowBase + (horSpacing * (index - 7)), paint);
                        canvas.drawText(PONUMBER, colBase + 80, rowBase + (horSpacing * (index - 7)), paint);
                        canvas.drawText(roundOneDecimal(GALLONS), colBase + 130, rowBase + (horSpacing * (index - 7)), paint);
                        canvas.drawText(TOTAL, colBase + 160, rowBase + (horSpacing * (index - 7)), paint);
                    }
                    index++;
                } while (cursorExpenses.moveToNext());
                cursorExpenses.close();
            }
        }

        @Override
        public void onWrite(final PageRange[] pageRanges,
                            final ParcelFileDescriptor destination,
                            final CancellationSignal
                                    cancellationSignal,
                            final WriteResultCallback callback) {
            for (int i = 0; i < totalpages; i++) {
                if (pageInRange(pageRanges, i))
                {
                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                            pageHeight, i).create();

                    PdfDocument.Page page =
                            myPdfDocument.startPage(newPage);

                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        myPdfDocument.close();
                        myPdfDocument = null;
                        return;
                    }
                    drawPage(page, i);
                    myPdfDocument.finishPage(page);
                }
            }

            try {
                myPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
                createFile(LoadNumber.toString(), myPdfDocument);
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                myPdfDocument.close();
                myPdfDocument = null;
            }

            callback.onWriteFinished(pageRanges);

        }

    }


    public void printDocument()
    {
        PrintManager printManager = (PrintManager) context
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        if (printManager != null) {
            printManager.print(jobName, new MyPrintDocumentAdapter(context),null);
        }
    }

    private boolean pageInRange(PageRange[] pageRanges, int page)
    {
        for (PageRange pageRange : pageRanges) {
            if ((page >= pageRange.getStart()) &&
                    (page <= pageRange.getEnd()))
                return true;
        }
        return false;
    }

    public void createFile(String fileName, PdfDocument document){
        FileOutputStream os = null;
        try {
            //Create folder in which you want to save Pdf documents
            File folder = new File(Environment.getDataDirectory(), "PdfFolder");
            if(!folder.exists()){
                if (folder.mkdirs()) {
                    Log.d(TAG, "createFile: " + folder + "created");
                }
            }
            //Create pdf file
            File mPdfFile = new File(folder,fileName);
            //Make sure that there isn't a file with the same name
            if(!mPdfFile.exists()) {
                if (mPdfFile.createNewFile()) {
                    Log.d(TAG, "createFile: " + mPdfFile + "created");
                }
            }
            os =  new FileOutputStream(mPdfFile);
            document.writeTo(os);
            Toast.makeText(getContext(), "File saved succesfully",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Something went wrong",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally{
            document.close();
            if(os!=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String roundOneDecimal(String dString)
    {
        double d = Double.parseDouble(dString);
        long iPart;
        double fPart;
        DecimalFormat twoDForm;

        iPart = (long) d;
        fPart = d - iPart;
        if (iPart >= 100) {
            twoDForm = new DecimalFormat("#.");
        } else if (iPart >= 10) {
            twoDForm = new DecimalFormat("#.#");
        } else {
            twoDForm = new DecimalFormat("#.##");
        }
        d =  Double.valueOf(twoDForm.format(d));
        if (fPart == 0) {
            return String.valueOf(iPart);
        }
        return String.valueOf(d);
    }

    private void saveTripSheet (View view) {
        session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        //Get trip variables from session
        String USERCODE = user.get(SessionManager.KEY_USERNAME);
        String TRUCK_NO = user.get(SessionManager.KEY_TRUCK);
        Integer LOAD_NO = LoadNumber;
        String NAME = user.get(SessionManager.KEY_NAME);
        //get remaining trip variables
        myDb = new DatabaseHelper(getContext());
        myDb2 = new DatabaseHelperStops(getContext());

        Cursor cursor =  myDb.getLoadData(LoadNumber);
        String TRAILER = "_", BOLNUMBER = "_", PIECES = "??", WEIGHT = "??", SHIPDATEshort = "???";
        if (cursor.moveToFirst()) {
            TRAILER = cursor.getString(cursor.getColumnIndex(Config.TAG_TRLRNUMBER));
            BOLNUMBER = cursor.getString(cursor.getColumnIndex(Config.TAG_BOLNUMBER));
            PIECES = cursor.getString(cursor.getColumnIndex(Config.TAG_PIECES));
            WEIGHT = cursor.getString(cursor.getColumnIndex(Config.TAG_WEIGHT));

        }

        myDb2 = new DatabaseHelperStops(getContext());
        Cursor cursorShipper = myDb2.getFirstShipperData(LoadNumber);
        String SHIPDATE = "??", SHIPNAME = "??", SHIPCITYST = "??";
        String CONSNAME = "??", CONSCITYST = "??";
        String EXPENSE, TYPE, PONUMBER, GALLONS, TOTAL;
        if (cursorShipper.moveToFirst()) {
            SHIPDATE = cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPEARLYDATE));
            SHIPNAME = cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPCUSTNAME));
            SHIPCITYST = cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPCUSTCITY)) + ", " +
                    cursorShipper.getString(cursorShipper.getColumnIndex(Config.TAG_STOPCUSTSTATE));}
        cursorShipper.close();
        Cursor cursorConsignee = myDb2.getLastConsigneeData(LoadNumber);
        if (cursorConsignee.moveToFirst()) {
            CONSNAME = cursorConsignee.getString(cursorConsignee.getColumnIndex(Config.TAG_STOPCUSTNAME));
            CONSCITYST = cursorConsignee.getString(cursorConsignee.getColumnIndex(Config.TAG_STOPCUSTCITY)) + ", " +
                    cursorConsignee.getString(cursorConsignee.getColumnIndex(Config.TAG_STOPCUSTSTATE));}
        cursorConsignee.close();

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat output = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            Date dateValue = input.parse(SHIPDATE);
            SHIPDATEshort = output.format(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Bitmap myBitmap = Bitmap.createBitmap( 555, 750, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(myBitmap);
        view.draw(canvas);
        canvas.setBitmap(myBitmap);

        int titleBaseLine = 75;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        try {
            Drawable d = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                d = getResources().getDrawable(R.drawable.blank_trip_sheet, context.getTheme());
            } else {
                d = ContextCompat.getDrawable(context, R.drawable.blank_trip_sheet);
            }
            d.setBounds(0, 0, 555, 750);
            d.draw(canvas);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        paint.setTextSize(14);
        canvas.drawText(NAME, 175, titleBaseLine, paint);

        canvas.drawText(SHIPDATEshort, 370, titleBaseLine, paint);
        canvas.drawText(TRAILER, 450, titleBaseLine, paint);

        Integer dataBaseLine = 125;
        paint.setTextSize(14);
        char character;
        int index = 0;
        for (int i = (6 - USERCODE.length()); i < 6 ; i++) {
            character = USERCODE.charAt(index);
            canvas.drawText(Character.toString(character), 34 + (i * 14), dataBaseLine, paint);
            canvas.drawCircle(39 + (i * 14), 139 + ((character - 65) * 14), 5, paint);
            index++;
        }
        index = 0;
        for (int i = (6 - TRUCK_NO.length()); i < 6 ; i++) {
            character = TRUCK_NO.charAt(index);
            canvas.drawText(Character.toString(character), 140 + (i * 14), dataBaseLine, paint);
            canvas.drawCircle(146 + (i * 14), 139 + ((character - 48) * 14), 5, paint);
            index++;
        }
        index = 0;
        for (int i = (7 - LOAD_NO.toString().length()); i < 7 ; i++) {
            character = LOAD_NO.toString().charAt(index);
            canvas.drawText(Character.toString(character), 244 + (i * 14), dataBaseLine, paint);
            canvas.drawCircle(250 + (i * 14), 139 + ((character - 48) * 14), 5, paint);
            index++;
        }
        paint.setTextSize(10);
        canvas.drawText(SHIPNAME, 165, 345, paint);
        canvas.drawText(SHIPCITYST, 165, 358, paint);
        canvas.drawText(CONSNAME, 350, 345, paint);
        canvas.drawText(CONSCITYST, 350, 358, paint);
        canvas.drawText(BOLNUMBER, 155, 371, paint);
        canvas.drawText(PIECES, 385, 371, paint);
        canvas.drawText(WEIGHT, 460, 371, paint);

        //Place Expenses
        paint.setTextSize(8);
        Cursor cursorExpenses = myDb3.getExpenseData(LoadNumber);
        if (cursorExpenses != null) {
            index = 1;
            Integer rowBase = 538;
            Integer colBase = 135;
            Integer horSpacing = 13;
            cursorExpenses.moveToFirst();
            do {
                EXPENSE = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPDESC));
                TYPE = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPTYPE));
                PONUMBER = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPPONUM));
                GALLONS = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPGALLON));
                TOTAL = cursorExpenses.getString(cursorExpenses.getColumnIndex(Config.TAG_EXPAMOUNT));
                if (index < 8) {
                    canvas.drawText(EXPENSE, colBase, rowBase + (horSpacing * index), paint);
                    canvas.drawText(PaymentTypeCode(TYPE), colBase + 65, rowBase + (horSpacing * index), paint);
                    canvas.drawText(PONUMBER, colBase + 80, rowBase + (horSpacing * index), paint);
                    canvas.drawText(roundOneDecimal(GALLONS), colBase + 115, rowBase + (horSpacing * index), paint);
                    canvas.drawText(TOTAL, colBase + 135, rowBase + (horSpacing * index), paint);
                } else {
                    colBase = 305;
                    canvas.drawText(EXPENSE, colBase, rowBase + (horSpacing * (index - 7)), paint);
                    canvas.drawText(PaymentTypeCode(TYPE), colBase + 65, rowBase + (horSpacing * (index - 7)), paint);
                    canvas.drawText(PONUMBER, colBase + 80, rowBase + (horSpacing * (index - 7)), paint);
                    canvas.drawText(roundOneDecimal(GALLONS), colBase + 130, rowBase + (horSpacing * (index - 7)), paint);
                    canvas.drawText(TOTAL, colBase + 160, rowBase + (horSpacing * (index - 7)), paint);
                }
                index++;
            } while (cursorExpenses.moveToNext());
            cursorExpenses.close();
        }

        File mediaStorageDir = Environment.getExternalStorageDirectory();
        File Directory = new File(mediaStorageDir, "LoadImages");
        if (!Directory.exists()) {
            if (Directory.mkdirs()) {
                Toast.makeText(context, "Directory Created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Directory NOT Created", Toast.LENGTH_SHORT).show();
            }
        }
        String fileName = Directory + "/TripSheet_" + LoadNumber + ".jpg";
        OutputStream stream;
        try {
            stream = new FileOutputStream(fileName);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "saveTripSheet: " + e);
        }
        String authority = BuildConfig.APPLICATION_ID + ".provider";
        Uri uriFilename = FileProvider.getUriForFile(context, authority, new File(fileName));
        myDb.putScanURI(LoadNumber, "Trip_Sheet", uriFilename.toString());
    }
}
