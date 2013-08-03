#include <fstream>
#include <time.h>
#include <Windows.h>

#define TEN_MINUTES 600000

int main(int argc, char* argv[])
{
	using namespace std;

	DWORD dwTime = 0;
	LASTINPUTINFO lastInputInfo;
	lastInputInfo.cbSize = sizeof(LASTINPUTINFO);
	int monthDay = 0;
	char attendanceRecordPath[_MAX_PATH];
	char* applicationData = getenv("APPDATA");
	strcpy_s(attendanceRecordPath, applicationData);
	strcat_s(attendanceRecordPath, "\\Genghis");
	CreateDirectory(attendanceRecordPath, NULL);
	strcat_s(attendanceRecordPath, "\\AttendanceRecord");
	CreateDirectory(attendanceRecordPath, NULL);
	strcat_s(attendanceRecordPath, "\\attendance.csv"); 
	fstream record(attendanceRecordPath, ios_base::out|ios_base::app);
	while(true)
	{
		Sleep(TEN_MINUTES);	// 休息10分钟
		GetLastInputInfo(&lastInputInfo);
		if (::GetTickCount64() - lastInputInfo.dwTime < TEN_MINUTES) // 表示10分钟内有操作
		{
			const time_t nowtime = time(NULL);
			tm current_time = {0};
			localtime_s(&current_time, &nowtime);
			if (current_time.tm_mday != monthDay)
			{
				record<<endl;
				monthDay=current_time.tm_mday;
			}
			char timeStr[40];
			strftime(timeStr, 40, "%Y%m%d%H%M", &current_time);
			record<<timeStr<<",";
			record.flush();
		}
	}
	return 0;
}