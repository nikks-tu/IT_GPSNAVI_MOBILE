package com.techuva.vehicletracking.utils;

/**
 * Created by user on 01/29/2020.
 */
public interface Constants {
    //Test URL
    //String BASE_URL_TOKEN = "http://182.18.177.27:4815/VT/";
    //String BASE_URL_TOKEN = "http://54.214.254.175:4815/VT/";
    //Production URL
    String BASE_URL_TOKEN = "https://vtspush.datapollz.com/vtsconsumerapis/VT/";
    String GetCurrentCount = "Device/getVehicleStatusSummary";
    String CurrentData = "Service/currentdata";
    String LoginData = "login";
    String SignUp = "Register/user";
    String ForgetPassword = "forgotPassword";
    String DeallocateDevice = "deallocateTag";
    String DelocateDriver = "Vehicle/deallocateDriver";
    String HistoryData = "Service/GetPivotRawSensorData";
    String VersionCheck = "VersionCheckInfo";
    String GetAllVehicleInfo = "Distance/getAllVehicleDeviceInfo";
    String DeviceID = "DeviceID";
    String GetDeviceAllocationHistory = "getVehicleTagHistory";
    String GetDriverAllocationHistory = "VehicleMaster/getVehicleDriverHistory";
    String CompanyID ="CompanyID";
    String UserID ="UserID";
    String UserIDSelected ="UserIDSelected";
    String UserName ="UserName";
    String UserMailId ="UserMailId";
    String AppVersion = "1";
    String FontVersion = "0";
    String GetVehicalSummary = "Distance/getDeviceInfo";
    String SingleAccount="SingleAccount";
    String IsLoggedIn = "IsLoggedIn";
    String IsDefaultDeviceSaved = "IsDefaultDeviceSaved";
    String GetAvailableDeviceList = "Device/getUnallocatedDeviceList";
    String GetAvailableDriversList = "Vehicle/getUnallocatedDrivers";
    //Token 90 Day
    String AuthorizationKey = "Basic dGVjaHV2YS1jbGllbnQtbW9iaWxlOnNlY3JldA==";
    //Token 1 Hr
    //String AuthorizationKey = "Basic dGVjaHV2YS1jbGllbnQ6c2VjcmV0";
    String GrantType= "password";
    String AccessToken ="AccessToken";
    String RefreshToken ="RefreshToken";
    String SecondsToExpireToken ="SecondsToExpireToken";
    String DateToExpireToken ="DateToExpireToken";
    String IsSessionExpired = "IsSessionExpired";
    String NULL_STRING = "null";
    String DEVICE_IN_USE = "";
    String COMPANY_ID = "COMPANY_ID";
    String NotificationCount = "NotificationCount";
    String Latitude = "Latitude";
    String Longitude = "Longitude";
    String RegistrationNum = "RegistrationNum";
    String TotalDistance = "TotalDistance";
    String ReceivedTime = "ReceivedTime";
    String History = "History";
    String Home = "Home";
    String ChangePassword = "changePassword";
    String VehicleMasterID = "VehicleMasterID";
    String VehicleDetails = "VehicleDetails";

    String AllocateVehicleDevice = "insertVehicleTag";
    String ReAllocateVehicleDevice = "reallocateTag";
    String AllocateDriver = "Vehicle/allocateDriver";
    String ReAllocateDriver = "Vehicle/reallocateDriver";
    String INVENTORY_ID = "INVENTORY_ID";
    String VEHICLE_DRIVER_ID = "VEHICLE_DRIVER_ID" ;
    String IsDeviceAllocated = "IsDeviceAllocated";
    String IsDriverAllocated = "IsDriverAllocated";
}
