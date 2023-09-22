package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdvManager {
    private static String OS = null;
    private String processName;

    /**
     * Retrieves the name of the operating system.
     *
     * @return The name of the operating system.
     */
    public static String getOSName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS.toUpperCase();

    }

    /**
     * Kills all running emulator processes based on the operating system.
     */
    public void killEmulatorProcesses() {
        if (getOSName().contains("Windows".toUpperCase())) {
            CommandLine();
        }
        if (getOSName().contains("IOS".toUpperCase())) {
            Terminal();
        }
    }

    /**
     * Executes the necessary commands through the command line to kill all running emulator processes on a Windows operating system.
     * This method uses the adb command to kill the emulator-5554 emulator process and then verifies if the qemu-system-x86_64.exe process is running.
     * If the process is running, it kills the process.
     */
    public void CommandLine() {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("adb -s emulator-5554 emu kill");
            Thread.sleep(3000);
            WindowsProcess("qemu-system-x86_64.exe");
            if(isRunning())
            {
                kill();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes the necessary command through the command line to kill the emulator process on a Windows or Unix-like operating system.
     * This method uses the adb command to kill the emulator process.
     */
    public static void Terminal() {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("adb -e emu kill");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes a new instance of the WindowsProcess class with the specified process name.
     *
     * @param processName The name of the process to be executed.
     */
    public void WindowsProcess(String processName)
    {
        this.processName = processName;
    }

    /**
     * Terminates the specified process if it is currently running.
     *
     * @throws Exception If the termination fails or if an error occurs while executing the termination command.
     */
    public void kill() throws Exception
    {
        if (isRunning())
        {
            getRuntime().exec("taskkill /F /IM " + processName);
        }
    }

    /**
     * Checks if the specified process is currently running.
     *
     * @return true if the process is running, false otherwise.
     * @throws Exception If an error occurs while checking the process status.
     */
    private boolean isRunning() throws Exception
    {
        Process listTasksProcess = getRuntime().exec("tasklist");
        BufferedReader tasksListReader = new BufferedReader(
                new InputStreamReader(listTasksProcess.getInputStream()));

        String tasksLine;

        while ((tasksLine = tasksListReader.readLine()) != null)
        {
            if (tasksLine.contains(processName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the instance of the Runtime class.
     *
     * @return The instance of the Runtime class.
     */
    private Runtime getRuntime()
    {
        return Runtime.getRuntime();
    }
}
