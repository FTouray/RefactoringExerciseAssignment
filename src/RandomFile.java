import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class RandomFile {
	// The single instance of this class
	private static RandomFile instance;

	// RandomAccessFile references for reading/writing
	private RandomAccessFile output;
	private RandomAccessFile input;

	// Private constructor to prevent instantiation from outside this class
	private RandomFile() {
	}

	// Public method to get the single instance (thread-safe)
	public static RandomFile getInstance() {
		if (instance == null) {
			synchronized (RandomFile.class) {
				if (instance == null) {
					instance = new RandomFile();
				}
			}
		}
		return instance;
	}

	// Create new file
	public void createFile(String fileName) {
		try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
			// File created successfully
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error creating file!");
		} 
	} // end createFile

	// Open file for adding or changing records
	public void openWriteFile(String fileName) {
		try {
			output = new RandomAccessFile(fileName, "rw");
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "File does not exist!");
		}
	} // end openWriteFile

	// Close file for adding or changing records
	public void closeWriteFile() {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error closing file!");
			System.exit(1);
		}
	} // end closeWriteFile

	// Add records to file
	public long addRecords(Employee employeeToAdd) {
		Employee newEmployee = employeeToAdd;
		long currentRecordStart = 0;
		RandomAccessEmployeeRecord record;

		try {
			record = new RandomAccessEmployeeRecord(
					newEmployee.getEmployeeId(),
					newEmployee.getPpsNumber(),
					newEmployee.getSurname(),
					newEmployee.getFirstName(),
					newEmployee.getGender(),
					newEmployee.getDepartment(),
					newEmployee.getSalary(),
					newEmployee.getFullTime());

			output.seek(output.length()); // Move to end of file
			record.write(output); // Write object to file
			currentRecordStart = output.length();
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error writing to file!");
		}

		// Return position where object starts in the file
		return currentRecordStart - RandomAccessEmployeeRecord.SIZE;
	} // end addRecords

	// Change details for existing object
	public void changeRecords(Employee newDetails, long byteToStart) {
		long currentRecordStart = byteToStart;
		RandomAccessEmployeeRecord record;
		try {
			record = new RandomAccessEmployeeRecord(
					newDetails.getEmployeeId(),
					newDetails.getPpsNumber(),
					newDetails.getSurname(),
					newDetails.getFirstName(),
					newDetails.getGender(),
					newDetails.getDepartment(),
					newDetails.getSalary(),
					newDetails.getFullTime());

			output.seek(currentRecordStart);
			record.write(output);
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error writing to file!");
		}
	} // end changeRecords

	// Delete existing object
	public void deleteRecords(long byteToStart) {
		long currentRecordStart = byteToStart;
		RandomAccessEmployeeRecord record;
		try {
			record = new RandomAccessEmployeeRecord(); // Empty object
			output.seek(currentRecordStart);
			record.write(output); // Replace existing object with empty object
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error writing to file!");
		}
	} // end deleteRecords

	// Open file for reading
	public void openReadFile(String fileName) {
		try {
			input = new RandomAccessFile(fileName, "r");
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "File is not supported!");
		}
	} // end openReadFile

	// Close file (reading)
	public void closeReadFile() {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error closing file!");
			System.exit(1);
		}
	} // end closeReadFile

	// Get position of the first record in the file
	public long getFirst() {
		long byteToStart = 0;
		try {
			input.length(); // Just checking the length, though not strictly necessary
		} catch (IOException e) {
			// swallow silently or handle as needed
		}
		return byteToStart;
	} // end getFirst

	// Get position of the last record in the file
	public long getLast() {
		long byteToStart = 0;
		try {
			byteToStart = input.length() - RandomAccessEmployeeRecord.SIZE;
		} catch (IOException e) {
			// swallow silently or handle
		}
		return byteToStart;
	} // end getLast

	// Get position of the next record in the file
	public long getNext(long readFrom) {
		long byteToStart = readFrom;
		try {
			if (byteToStart + RandomAccessEmployeeRecord.SIZE == input.length()) {
				byteToStart = 0;
			} else {
				byteToStart += RandomAccessEmployeeRecord.SIZE;
			}
		} catch (NumberFormatException | IOException e) {
			// swallow silently or handle
		}
		return byteToStart;
	} // end getNext

	// Get position of the previous record in the file
	public long getPrevious(long readFrom) {
		long byteToStart = readFrom;
		try {
			if (byteToStart == 0) {
				byteToStart = input.length() - RandomAccessEmployeeRecord.SIZE;
			} else {
				byteToStart -= RandomAccessEmployeeRecord.SIZE;
			}
		} catch (NumberFormatException | IOException e) {
			// swallow silently or handle
		}
		return byteToStart;
	} // end getPrevious

	// Get Employee object from file at specified position
	public Employee readRecords(long byteToStart) {
		Employee thisEmp = null;
		RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();
		try {
			input.seek(byteToStart);
			record.read(input);
		} catch (IOException e) {
			// swallow silently or handle
		}
		thisEmp = record;
		return thisEmp;
	} // end readRecords

	// Check if PPS Number already exists
	public boolean isPpsExist(String pps, long currentByteStart) {
		RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();
		boolean ppsExist = false;
		long oldByteStart = currentByteStart;
		long currentByte = 0;

		try {
			// Loop until PPS is found or we reach end of file
			while (currentByte != input.length() && !ppsExist) {
				if (currentByte != oldByteStart) {
					input.seek(currentByte);
					record.read(input);
					if (record.getPpsNumber().trim().equalsIgnoreCase(pps)) {
						ppsExist = true;
						JOptionPane.showMessageDialog(null, "PPS number already exists!");
					}
				}
				currentByte += RandomAccessEmployeeRecord.SIZE;
			}
		} catch (IOException e) {
			// swallow silently or handle
		}
		return ppsExist;
	} // end isPpsExist

	// Check if any record contains a valid ID (greater than 0)
	public boolean isSomeoneToDisplay() {
		boolean someoneToDisplay = false;
		long currentByte = 0;
		RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();

		try {
			while (currentByte != input.length() && !someoneToDisplay) {
				input.seek(currentByte);
				record.read(input);
				if (record.getEmployeeId() > 0) {
					someoneToDisplay = true;
				}
				currentByte += RandomAccessEmployeeRecord.SIZE;
			}
		} catch (IOException e) {
			// swallow silently or handle
		}
		return someoneToDisplay;
	} // end isSomeoneToDisplay
} // end class RandomFile
