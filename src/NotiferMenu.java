import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import java.awt.Component;
import java.awt.Desktop;

import javax.swing.Box;
import javax.swing.Icon;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JEditorPane;
import javax.swing.DropMode;
import java.awt.Label;
import javax.swing.JLayeredPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextArea;


public class NotiferMenu {

	private JFrame frame;
	private static JTextField inpt_rate;
	private final Action action = new SwingAction();
	
	static double app_version = 1.0;
	
	// Application Settings (settings that the user is able to change via the GUI)	
	/** Time to look for newer jobs
	 *  Units are in seconds!*/
	static int refresh_timer;
	
	/** Check box for auto refresh setting*/
	static JCheckBox app_enbl_checkbox;
	
	/** Check box for pop up alerts setting.*/
	static JCheckBox app_enbl_alerts;
	
	/** Check box for logging manual actions*/
	static JCheckBox app_enbl_actionlog;
	
	/** Check box for logging latest job msg*/
	static JCheckBox app_enbl_latestlog;
	
	/** Check box for logging last refresh*/
	static JCheckBox app_enbl_refreshlog;
	
	
	
	
	// Application fields that are used to easily grab GUI objects to add and remove components
	/** Whether or not the program has been initialized already*/
	static boolean app_initialized = false;
	
	/** Whether or not manual refresh button was created.*/
	static boolean manual_refresh_isvalid = false;
	
	/** Manual refresh button*/
	JButton app_man_ref_btn;
	
	/** Latest job listing's ID.
	 *  Used to see if there is a new job listing or not.*/
	static int app_last_latestid;
	
	/** Whether or not the program is in the process of refreshing*/
	static boolean app_isRefreshing = false;
	
	/** The WebScrape instance. Used to execute webscraping related tasks.*/
	static WebScrape app_w;
	
	/** Main panel of the application.
	 *  Contains manual action buttons, progress bar, latest job listing panel and job listings panel*/
	static JPanel app_main;
	
	/** Individual job listings stored in array to more easily iterate
	 *  display job listings that come before the latest job listing.
	 *  There will always being 6 job listings.*/
	static JPanel[] job_panels = new JPanel[6];
	
	/** keys are job_nums which range from 0..5
	 *  values are respective job IDs
	 *  Ex) job0's ID is the next valid ID that comes before latest ID.*/
	static HashMap<String, Integer> job_ids = new HashMap<String, Integer>();
	
	
	// Various GUI components used for easier manipulation
	/** Status label that is on main_panel*/
	static JLabel app_lbl_status;
	
	/** Application status that is used for back-end
	 * 	Can be one of the following: "START", "RUN", "STOP", "PAUSE"*/
	 static String app_status;
	
	/** Latest job listing panel*/
	static JPanel app_latestpanel;
	
	/** Job listings panel*/
	static JPanel app_listingspanel;
	
	/** Refresh timer progress bar*/
	static JProgressBar app_refreshbar;
	
	/** Application console window
	 * 
	 * 	Note: that the console is made up of a JTextArea with a JScrollPane over it.
	 *  This is important for when wanting to call certain methods on the object.*/
	static JTextArea app_log_obj;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotiferMenu window = new NotiferMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Create a menu and GmodStore instance.
	 */
	public NotiferMenu() {
		load_MainFrame();
	}
	

	/**
	 * Creates general framework of the application. The content is indirectly
	 * related to jobs info and is mostly used to create spacing for job content.
	 */
	private void load_MainFrame() {
		WebScrape w = new WebScrape();
		app_w = w;
		Image icon = Toolkit.getDefaultToolkit().getImage("src\\Images/icon.png");
		

		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.menu);
		frame.setBounds(100, 100, 1072, 683);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("GmodStore Job Notifier"+ " - Version " + app_version);
		frame.setIconImage(icon);
		
		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/main.png")).getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH)));
		logo.setBounds(10, 10, 125, 125);
		frame.getContentPane().add(logo);
		
		JSeparator logo_sep_vert = new JSeparator();
		logo_sep_vert.setOrientation(SwingConstants.VERTICAL);
		logo_sep_vert.setBounds(145, 10, 15, 626);
		frame.getContentPane().add(logo_sep_vert);
		
		JSeparator logosep = new JSeparator();
		logosep.setBounds(10, 145, 136, 10);
		frame.getContentPane().add(logosep);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		panel.setBorder(new LineBorder(new Color(153, 180, 209), 2, true));
		panel.setBounds(10, 155, 125, 480);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setBounds(18, 7, 88, 24);
		lblSettings.setLabelFor(frame);
		lblSettings.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 20));
		lblSettings.setToolTipText("Modify the program's options here!");
		panel.add(lblSettings);
		
		JSeparator refresh_sep = new JSeparator();
		refresh_sep.setBounds(16, 79, 90, 2);
		panel.add(refresh_sep);
		
		JCheckBox enbl_autorefresh = new JCheckBox("Auto Refresh");
		enbl_autorefresh.setFont(new Font("Tahoma", Font.PLAIN, 8));
		enbl_autorefresh.setBackground(SystemColor.inactiveCaptionBorder);
		enbl_autorefresh.setSelected(true);
		enbl_autorefresh.setToolTipText("Scan the GmodStore for jobs automatically?");
		enbl_autorefresh.setBounds(10, 87, 95, 21);
		panel.add(enbl_autorefresh);
		app_enbl_checkbox = enbl_autorefresh;
		
		inpt_rate = new JTextField();
		inpt_rate.setToolTipText("How often should new jobs be looked for? (seconds)");
		inpt_rate.setText("60");
		inpt_rate.setBounds(10, 130, 96, 19);
		panel.add(inpt_rate);
		inpt_rate.setColumns(10);
		refresh_timer = Integer.parseInt(inpt_rate.getText());
		
		JButton btn_submit_newtime = new JButton("Submit");
		btn_submit_newtime.setToolTipText("Apply new refresh rate");
		btn_submit_newtime.setFont(new Font("Arial", Font.PLAIN, 8));
		btn_submit_newtime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!inpt_rate.getText().matches("^[0-9]+$")) log("ERROR: Value must be a number!");
				
				int inpt_rate_text = Integer.parseInt(inpt_rate.getText());
				if (inpt_rate_text < 10 ) {
					log("ERROR: Value must be >= 10!");
				} else {
					refresh_timer = Integer.parseInt(inpt_rate.getText());
					log("Refresh timer changed to " + inpt_rate.getText() + " sec");
				}
			}
		});
		btn_submit_newtime.setBounds(20, 148, 78, 15);
		panel.add(btn_submit_newtime);
		
		JLabel lblSearchRate = new JLabel("Refresh Rate:");
		lblSearchRate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearchRate.setBounds(10, 114, 72, 15);
		panel.add(lblSearchRate);
		
		JLabel lblRefreshSettings = new JLabel("Refreshing");
		lblRefreshSettings.setFont(new Font("Arial", Font.BOLD, 12));
		lblRefreshSettings.setBounds(29, 62, 72, 14);
		panel.add(lblRefreshSettings);
		
		JLabel lblNotifcations = new JLabel("Notifcations");
		lblNotifcations.setFont(new Font("Arial", Font.BOLD, 12));
		lblNotifcations.setBounds(25, 189, 72, 14);
		panel.add(lblNotifcations);
		
		JSeparator notif_sep = new JSeparator();
		notif_sep.setBounds(15, 206, 90, 2);
		panel.add(notif_sep);
		
		JCheckBox enbl_notif = new JCheckBox("Popup Alerts");
		enbl_notif.setFont(new Font("Tahoma", Font.PLAIN, 8));
		enbl_notif.setToolTipText("Enable pop up alerts?");
		enbl_notif.setSelected(true);
		enbl_notif.setBackground(SystemColor.inactiveCaptionBorder);
		enbl_notif.setBounds(10, 214, 109, 21);
		panel.add(enbl_notif);
		app_enbl_alerts = enbl_notif;
		
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setFont(new Font("Arial", Font.BOLD, 12));
		lblLogs.setBounds(46, 257, 34, 14);
		panel.add(lblLogs);
		
		JSeparator logs_sep = new JSeparator();
		logs_sep.setBounds(16, 274, 90, 2);
		panel.add(logs_sep);
		
		JCheckBox enbl_loglatest = new JCheckBox("Latest offer");
		enbl_loglatest.setToolTipText("Log latest job offer?");
		enbl_loglatest.setSelected(true);
		enbl_loglatest.setFont(new Font("Tahoma", Font.PLAIN, 8));
		enbl_loglatest.setBackground(SystemColor.inactiveCaptionBorder);
		enbl_loglatest.setBounds(10, 282, 109, 21);
		panel.add(enbl_loglatest);
		app_enbl_latestlog = enbl_loglatest;
		
		JCheckBox enbl_logrefresh = new JCheckBox("Last refresh");
		enbl_logrefresh.setToolTipText("Log when last refreshed?");
		enbl_logrefresh.setSelected(false);
		enbl_logrefresh.setFont(new Font("Tahoma", Font.PLAIN, 8));
		enbl_logrefresh.setBackground(SystemColor.inactiveCaptionBorder);
		enbl_logrefresh.setBounds(10, 305, 109, 21);
		panel.add(enbl_logrefresh);
		app_enbl_refreshlog = enbl_logrefresh;
		
		JCheckBox enbl_logactions = new JCheckBox("Manual actions");
		enbl_logactions.setToolTipText("Log manual actions? (play, stop, pause)");
		enbl_logactions.setSelected(true);
		enbl_logactions.setFont(new Font("Tahoma", Font.PLAIN, 8));
		enbl_logactions.setBackground(SystemColor.inactiveCaptionBorder);
		enbl_logactions.setBounds(10, 327, 109, 21);
		panel.add(enbl_logactions);
		app_enbl_actionlog = enbl_logactions;
		
		JLabel lblCredits = new JLabel("Credits");
		lblCredits.setToolTipText("Created by Anthony DeArmas");
		lblCredits.setBackground(SystemColor.inactiveCaption);
		lblCredits.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		lblCredits.setBounds(35, 452, 53, 18);
		panel.add(lblCredits);
		
		JSeparator credits_sep = new JSeparator();
		credits_sep.setForeground(SystemColor.activeCaption);
		credits_sep.setBackground(SystemColor.activeCaption);
		credits_sep.setBounds(2, 440, 121, 2);
		panel.add(credits_sep);
		
		JPanel main_panel = new JPanel();
		main_panel.setBorder(new LineBorder(SystemColor.activeCaption, 2, true));
		main_panel.setBackground(SystemColor.inactiveCaptionBorder);
		main_panel.setBounds(167, 10, 869, 626);
		frame.getContentPane().add(main_panel);
		main_panel.setLayout(null);
		app_main = main_panel;
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 134, 849, 8);
		main_panel.add(separator_1);
		
		JButton bttn_run = new JButton("");
		bttn_run.setToolTipText("Run the program");
		bttn_run.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icon_play.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		bttn_run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (app_initialized) {
					if (app_status == "RUN") {
						log("Program is already running!");
						return;
					}
					setStatus("RUN");
					if (app_enbl_actionlog.isSelected()) log("Manually ran the program!");
				}
			}
		});
		bttn_run.setFont(new Font("Arial", Font.BOLD, 25));
		bttn_run.setForeground(new Color(50, 205, 50));
		bttn_run.setBackground(UIManager.getColor("activeCaption"));
		bttn_run.setBounds(623, 140, 72, 39);
		main_panel.add(bttn_run);
		
		JButton bttn_stop = new JButton("");
		bttn_stop.setToolTipText("Stop the program");
		bttn_stop.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icon_stop.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		bttn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (app_initialized) {
					if (app_status == "STOP") {
						log("Program is already stopped!");
						return;
					}
					setStatus("STOP");
					if (app_enbl_actionlog.isSelected()) log("Manually stopped the program!");
				}
			}
		});
		bttn_stop.setForeground(new Color(255, 0, 0));
		bttn_stop.setFont(new Font("Arial", Font.BOLD, 25));
		bttn_stop.setBackground(UIManager.getColor("activeCaption"));
		bttn_stop.setBounds(705, 140, 72, 39);
		main_panel.add(bttn_stop);
		
		JButton bttn_pause = new JButton("");
		bttn_pause.setToolTipText("Pause the program");
		bttn_pause.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icon_pause.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		bttn_pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (app_initialized) {
					if (app_status == "PAUSE") {
						log("Program is already paused!");
						return;
					}
					setStatus("PAUSE");
					if (app_enbl_actionlog.isSelected()) log("Manually paused the program!");
				}
			}
		});
		bttn_pause.setForeground(Color.ORANGE);
		bttn_pause.setFont(new Font("Arial", Font.BOLD, 25));
		bttn_pause.setBackground(UIManager.getColor("activeCaption"));
		bttn_pause.setBounds(787, 140, 72, 39);
		main_panel.add(bttn_pause);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setBackground(SystemColor.activeCaptionBorder);
		progressBar.setValue(0);
		progressBar.setForeground(new Color(100, 149, 237));
		progressBar.setToolTipText("Displays the time until next refresh");
		progressBar.setBounds(10, 141, 603, 38);
		main_panel.add(progressBar);
		app_refreshbar = progressBar;
		
		JLabel lbl_statustext = new JLabel("Status:");
		lbl_statustext.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 72));
		lbl_statustext.setBounds(34, 25, 812, 85);
		main_panel.add(lbl_statustext);
		
		JLabel lbl_status = new JLabel();
		lbl_status.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 72));
		lbl_status.setBounds(344, 25, 515, 85);
		main_panel.add(lbl_status);
		app_lbl_status = lbl_status;
		
		JButton btnManualRefresh = new JButton("Manual Refresh");
		btnManualRefresh.setBounds(706, 109, 153, 21);
		btnManualRefresh.setVisible(false);
		main_panel.add(btnManualRefresh);
		app_man_ref_btn = btnManualRefresh;
		
	
		JPanel panel_console = new JPanel();
		panel_console.setLayout(null);
		panel_console.setBorder(new LineBorder(new Color(153, 180, 209), 1, true));
		panel_console.setBounds(10, 452, 278, 164);
		main_panel.add(panel_console);
		
		JTextArea app_log = new JTextArea();
		app_log.setDropMode(DropMode.INSERT);
		app_log.setFont(new Font("Arial", Font.BOLD, 12));
		app_log.setForeground(Color.GREEN);
		app_log.setBackground(Color.BLACK);
		app_log.setEditable(false);
		app_log.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(app_log);
		scroll.setBounds(10, 10, 258, 144);
		panel_console.add(scroll);
		app_log_obj = app_log;
		
		if (!app_initialized) {
			log("Application initializing...");
			setStatus("START");
		}
		
		JPanel panel_latestjob = new JPanel();
		panel_latestjob.setBorder(new LineBorder(SystemColor.activeCaption, 1, true));
		panel_latestjob.setBounds(10, 189, 278, 253);
		main_panel.add(panel_latestjob);
		panel_latestjob.setLayout(null);
		app_latestpanel = panel_latestjob;
		
		JPanel panel_joblistings = new JPanel();
		panel_joblistings.setBorder(new LineBorder(new Color(153, 180, 209), 1, true));
		panel_joblistings.setBounds(298, 189, 561, 427);
		main_panel.add(panel_joblistings);
		panel_joblistings.setLayout(null);
		
		JLabel lblLatestJobListing = new JLabel("Latest Job Listings");
		lblLatestJobListing.setFont(new Font("Arial", Font.BOLD, 20));
		lblLatestJobListing.setBounds(185, 10, 189, 24);
		panel_joblistings.add(lblLatestJobListing);
		app_listingspanel = panel_joblistings;
		
		JSeparator latestlistings_sep = new JSeparator();
		latestlistings_sep.setBounds(30, 36, 507, 2);
		panel_joblistings.add(latestlistings_sep);

		
		load_PrimaryJobsInfo(panel_latestjob, panel_joblistings, w);
	}

	/** Sets up latest job info and sets up more panels/framework for jobs that are displayed
	 *  in the job listings panel.*/
	private void load_PrimaryJobsInfo(JPanel latestjob_panel, JPanel joblistings_panel, WebScrape w){
		JLabel lblLatestJobOffering = new JLabel("Latest Job Offer");
		lblLatestJobOffering.setFont(new Font("Arial", Font.BOLD, 20));
		lblLatestJobOffering.setBounds(55, 10, 189, 24);
		latestjob_panel.add(lblLatestJobOffering);
		
		JSeparator latestjob_sep = new JSeparator();
		latestjob_sep.setBounds(23, 36, 218, 2);
		latestjob_panel.add(latestjob_sep);
		
		JLabel lblLatestJobListing = new JLabel("Latest Job Listings");
		lblLatestJobListing.setFont(new Font("Arial", Font.BOLD, 20));
		lblLatestJobListing.setBounds(185, 10, 189, 24);
		joblistings_panel.add(lblLatestJobListing);

		JSeparator latestlistings_sep = new JSeparator();
		latestlistings_sep.setBounds(30, 36, 507, 2);
		joblistings_panel.add(latestlistings_sep);
		
		JLabel lbl_loading = new JLabel("Loading...");
		lbl_loading.setFont(new Font("Arial", Font.PLAIN, 24));
		lbl_loading.setBounds(90, 112, 106, 28);
		latestjob_panel.add(lbl_loading);
		
		Timer initial_timer = new Timer(25, new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			int latestjob_id = w.getLatestID();
			app_last_latestid = latestjob_id;
			
			String latestjob_name_pretty = w.getJobName(latestjob_id);
			String latestjob_name = latestjob_name_pretty;
			if (latestjob_name_pretty != null && latestjob_name_pretty.length() >= 32) latestjob_name_pretty = latestjob_name_pretty.subSequence(0,  27) + "...";
			JLabel lbl_latestname = new JLabel("Job Name:" + latestjob_name_pretty);
			lbl_latestname.setFont(new Font("Arial", Font.PLAIN, 14));
			lbl_latestname.setBounds(23, 48, 218, 19);
			if (latestjob_name_pretty.length() >= 18)  lbl_latestname.setToolTipText(latestjob_name);
			latestjob_panel.add(lbl_latestname);
			
			JLabel lbl_latestbudget = new JLabel("Budget: "  + w.getJobBudget(latestjob_id));
			lbl_latestbudget.setFont(new Font("Arial", Font.PLAIN, 14));
			lbl_latestbudget.setBounds(23, 77, 218, 19);
			latestjob_panel.add(lbl_latestbudget);
			
			JButton btn_gotolatest = new JButton("Visit Latest Job");
			btn_gotolatest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					w.OpenJob(latestjob_id);
				}
			});
			btn_gotolatest.setAction(action);
			btn_gotolatest.setText("Visit Latest Job");
			btn_gotolatest.setBackground(UIManager.getColor("Button.highlight"));
			btn_gotolatest.setToolTipText("Open the job in a new tab");
			btn_gotolatest.setFont(new Font("Arial", Font.BOLD, 12));
			btn_gotolatest.setBounds(10, 222, 258, 21);
			latestjob_panel.add(btn_gotolatest);
			
			JLabel lbl_latestduedate = new JLabel("Due Date: " + w.getJobDate( latestjob_id ));
			lbl_latestduedate.setFont(new Font("Arial", Font.PLAIN, 14));
			lbl_latestduedate.setBounds(23, 106, 221, 19);
			latestjob_panel.add(lbl_latestduedate);
			
			JLabel lbl_latestcat = new JLabel("Category: " + w.getJobCategory( latestjob_id ));
			lbl_latestcat.setFont(new Font("Arial", Font.PLAIN, 14));
			lbl_latestcat.setBounds(23, 135, 221, 19);
			latestjob_panel.add(lbl_latestcat);
			
			JLabel lbl_latestapplicants = new JLabel("Applicants: " + w.getJobApplicants(latestjob_id));
			lbl_latestapplicants.setFont(new Font("Arial", Font.PLAIN, 14));
			lbl_latestapplicants.setBounds(23, 164, 221, 19);
			latestjob_panel.add(lbl_latestapplicants);
			
			JLabel lbl_latestviews = new JLabel("Views: " + w.getJobViews(latestjob_id));
			lbl_latestviews.setFont(new Font("Arial", Font.PLAIN, 14));
			lbl_latestviews.setBounds(23, 193, 221, 19);
			latestjob_panel.add(lbl_latestviews);
			
			JPanel job1 = new JPanel();
			job1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			job1.setBounds(30, 60, 163, 172);
			joblistings_panel.add(job1);
			job1.setLayout(null);

			JPanel job2 = new JPanel();
			job2.setLayout(null);
			job2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			job2.setBounds(203, 60, 163, 172);
			joblistings_panel.add(job2);

			JPanel job3 = new JPanel();
			job3.setLayout(null);
			job3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			job3.setBounds(376, 60, 163, 172);
			joblistings_panel.add(job3);

			JPanel job4 = new JPanel();
			job4.setLayout(null);
			job4.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			job4.setBounds(30, 242, 163, 172);
			joblistings_panel.add(job4);
			
			JPanel job5 = new JPanel();
			job5.setLayout(null);
			job5.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			job5.setBounds(203, 242, 163, 172);
			joblistings_panel.add(job5);

			JPanel job6 = new JPanel();
			job6.setLayout(null);
			job6.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			job6.setBounds(374, 242, 163, 172);
			joblistings_panel.add(job6);

			latestjob_panel.remove(lbl_loading);
			latestjob_panel.validate();
			latestjob_panel.repaint();
			joblistings_panel.validate();
			joblistings_panel.repaint();
			
			if (app_enbl_latestlog.isSelected()) log("Loaded latest job #" + latestjob_id  + "!");

			job_panels[0] = job1;
			job_panels[1] = job2;
			job_panels[2] = job3;
			job_panels[3] = job4;
			job_panels[4] = job5;
			job_panels[5] = job6;
			
			job_ids.put("job0", w.getNextID( w.id_latest ));
			job_ids.put("job1", w.getNextID( job_ids.get("job0") ));
			job_ids.put("job2", w.getNextID( job_ids.get("job1") ));
			job_ids.put("job3", w.getNextID( job_ids.get("job2") ));
			job_ids.put("job4", w.getNextID( job_ids.get("job3") ));
			job_ids.put("job5", w.getNextID( job_ids.get("job4") ));
			
			load_SecondaryJobInfo(w, 0);
	
	    }
		});
		initial_timer.start();
		initial_timer.setRepeats(false);
	}
	
	private void load_SecondaryJobInfo(WebScrape w, int job_num){
		
			if (job_num >= 6 && !app_initialized) {
				log("Initialization is now complete!" );
				setStatus("RUN");
				return;
			} else if (job_num >= 6) {
				return;
			}
		
			JPanel jobpanel = job_panels[job_num];
			int job_id = job_ids.get("job" + job_num);
						
			JLabel lbl_loading = new JLabel("Loading...");
			lbl_loading.setFont(new Font("Arial", Font.PLAIN, 12));
			lbl_loading.setBounds(55, 70, 80, 14);
			jobpanel.add(lbl_loading);
			jobpanel.getParent().validate();
			jobpanel.getParent().repaint();
		
			Timer nolag_timer = new Timer(2000, new ActionListener(){
				@Override
					public void actionPerformed(ActionEvent e) {
							
							JLabel lbl_name_job = new JLabel("Job Name:" + w.getJobName( job_id ));
							lbl_name_job.setToolTipText(lbl_name_job.getText());
							lbl_name_job.setFont(new Font("Arial", Font.PLAIN, 12));
							lbl_name_job.setBounds(10, 10, 143, 13);
							jobpanel.add(lbl_name_job);
							
							JLabel lbl_budget_job = new JLabel("Budget: " + w.getJobBudget( job_id ));
							lbl_budget_job.setFont(new Font("Arial", Font.PLAIN, 12));
							lbl_budget_job.setBounds(10, 33, 143, 13);
							jobpanel.add(lbl_budget_job);
							
							JLabel lbl_duedate_job = new JLabel("Due Date: " + w.getJobDate( job_id ));
							lbl_duedate_job.setFont(new Font("Arial", Font.PLAIN, 12));
							lbl_duedate_job.setBounds(10, 56, 143, 13);
							jobpanel.add(lbl_duedate_job);
							
							JLabel lbl_category_job = new JLabel("Category: " + w.getJobCategory( job_id ));
							lbl_category_job.setFont(new Font("Arial", Font.PLAIN, 12));
							lbl_category_job.setBounds(10, 79, 143, 13);
							jobpanel.add(lbl_category_job);
							
							JLabel lbl_applicants_job = new JLabel("Applicants: " + w.getJobApplicants( job_id ));
							lbl_applicants_job.setFont(new Font("Arial", Font.PLAIN, 12));
							lbl_applicants_job.setBounds(10, 102, 143, 13);
							jobpanel.add(lbl_applicants_job);
							
							JLabel lbl_visit_job = new JLabel("Views: " + w.getJobViews( job_id ));
							lbl_visit_job.setFont(new Font("Arial", Font.PLAIN, 12));
							lbl_visit_job.setBounds(10, 125, 143, 13);
							jobpanel.add(lbl_visit_job);
							
							JButton bttn_visit_job = new JButton("Visit");
							bttn_visit_job.setFont(new Font("Arial", Font.PLAIN, 16));
							bttn_visit_job.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									w.OpenJob( job_id );
								}
							});
							bttn_visit_job.setText("Visit");
							bttn_visit_job.setBounds(10, 145, 143, 21);
							jobpanel.add(bttn_visit_job);
						
							jobpanel.remove(lbl_loading);
							jobpanel.validate();
							jobpanel.repaint();
							
							log("Loaded job #" + job_id + "!");

							load_SecondaryJobInfo(w, job_num+1);
					
					    }
					});
			nolag_timer.start();
			nolag_timer.setRepeats(false);
		}
	
		/** Sets the status of the program and starts the AutoRefresh timer
		 *  if it is the first time running the application.
		 *  
		 *  Precondition: status must be one of the following: "START", "RUN", "STOP", "PAUSE"*/
		private void setStatus(String status) {
			assert status.equals("START") || status.equals("RUN") || status.equals("STOP") || status.equals("PAUSE");
			
			HashMap<String, String> status_names = new HashMap<String, String>();
			status_names.put("START", "Initializing");
			status_names.put("RUN", "Running");
			status_names.put("STOP", "Stopped");
			status_names.put("PAUSE", "Paused");
			
			
			if (status == "START");
			
			if (status == "RUN") {				
				if (!app_initialized) {
					AutoRefresh();
				}
				
				app_initialized = true;

			}
			
			if (status == "STOP");
			if (status == "PAUSE");
			
			if (app_status != status) app_status = status;
			app_lbl_status.setText(status_names.get(status));
			app_lbl_status.revalidate();
			app_lbl_status.repaint();
		}
	
		/** Adds log messages to the console.*/
		private void log(String msg) {
			String time = "" + java.time.LocalTime.now();
			String console_time = " <" + time.substring(0, 5) + "> ";
			app_log_obj.append(console_time + msg + "\n");
		}	
		
		private void AutoRefresh() {
			Timer barprogress_timer = new Timer(1000, new ActionListener(){
			int  counter = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!app_enbl_checkbox.isSelected()) {
					 if (!manual_refresh_isvalid) {
						app_man_ref_btn.setVisible(true);
						app_main.revalidate();
						app_main.repaint();
						manual_refresh_isvalid = true;
					 }
				} else {
					app_man_ref_btn.setVisible(false);
					app_main.revalidate();
					app_main.repaint();
					manual_refresh_isvalid = false;
				}
				


					
					if (!app_isRefreshing && (app_status == "RUN")) {
						if ((counter) >= refresh_timer) {
								if (app_enbl_refreshlog.isSelected()) log("Checking for new job listing...");
									
								if (app_last_latestid != app_w.getLatestID()) {
									if (app_enbl_alerts.isSelected()) {
										Image icon = Toolkit.getDefaultToolkit().getImage("src\\Images/icon.png");
									    Toolkit.getDefaultToolkit().beep();
									    JOptionPane newjob_notif = new JOptionPane("Latest job has been detected!", JOptionPane.INFORMATION_MESSAGE);
									    JDialog newjob_notif_msg = newjob_notif.createDialog("GmodStore Job Notification");
									    newjob_notif_msg.setIconImage(icon);
									    newjob_notif_msg.setAlwaysOnTop(true);
									    newjob_notif_msg.setVisible(true);
									}
									
									log("Latest job has been detected!");
									log("Refreshing jobs...");
									app_isRefreshing = true;
									app_listingspanel.removeAll();
									app_listingspanel.revalidate();
									app_listingspanel.repaint();
									
									app_latestpanel.removeAll();
									app_latestpanel.revalidate();
									app_latestpanel.repaint();
									load_PrimaryJobsInfo(app_latestpanel, app_listingspanel, app_w);
									app_isRefreshing = false;
								} else {
									if (app_enbl_refreshlog.isSelected()) log("No latest job changed detected...");
								}
								counter = 0;	
						}
						counter++;
						
						double bar_fill_math = (counter /  (double) refresh_timer);
						double bar_fill = bar_fill_math * (double) 100;
						app_refreshbar.setValue((int) bar_fill);
						app_refreshbar.setStringPainted(true);
						app_refreshbar.setString((refresh_timer - counter) + " seconds until next refresh...");
						app_refreshbar.revalidate();
						app_refreshbar.repaint();
						
					} else if (app_status == "STOP") {
						counter = 0;
						app_refreshbar.setValue(0);
						app_refreshbar.setStringPainted(false);
						app_refreshbar.revalidate();
						app_refreshbar.repaint();
					}
					
					if (!app_enbl_checkbox.isSelected()) {
						counter = 0;
						app_refreshbar.setValue(0);
						app_refreshbar.setStringPainted(false);
						app_refreshbar.revalidate();
						app_refreshbar.repaint();
					}
				}
			
			});
			barprogress_timer.start();
			barprogress_timer.setRepeats(true);	
		}

	/** Default JSwing action classed used for action listeners.*/
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
