# Food-Label-Scanner-App
An app that let's you scan labels of different food products and tells you how healthy are they depending on the ingredients

Pasi pentru configurarea si pornirea proiectului:

1  Se cloneaza sau descarca proiectul sub forma de arhiva .zip sau .7z si se dezarhiveaza

2  Se porneste Android Studio (Android Koala Feature Drop 2024.1.2 este versiunea)

3  Se verifica existenta urmatoarelor tool-uri in cadrul "Main Menu" -> "File" -> "Settings" -> "Languages and Frameworks" -> "Android SDK"

Pentru "SDK Platforms" sunt instalate: "Android 15.0 ("VanillaIceCream") API Level 35 (Revision 1)" si "Android 14.0 ("UpsideDownCAke") API Level 34 (Revision 3)"

Pentru "SDK Tools" sunt instalate: "Andrioid SDK Build-Tools 36" , "Android SDK Platform-Tools 35.0.2" , "Android Emulator 35.2.10", "Android Emulator hypervisor driver (installer) 2.2.0" , "Intel x86 Emulator Accelerator (HAXM installer) Deprecated 7.6.5" , "Google USB Driver 13"    

4  Se selecteaza "Open an Existing Project" si asteptam ca Android Studio sa indexeze si sa incarce proiectul

5  Trebuie avuta o conexiune la Internet pentru diversele importuri din cadrul proiectului

6  In caz de eroare se recomanda repornirea Android Studio, trebuie deschis in Android Studio folderul "FoodLabelScanner"

7  Daca nu apare un run configuration corect, se deschide prin "Edit Configurations" iar mai apoi in stanga sus pe "+" si ca modul se selecteaza "Food-Label-Scanner.app.main"

9a  Se selecteaza un emulator, de preferat "Medium Phone API 35" la care sa fie setat prin "Device Manager" -> "Medium Phone API 35 Settings" -> "Edit" -> "Show Advanced Settings" -> "Camera" -> Front : WebCam0 , Back : None - pentru a putea folosi camera laptop-ului sau o camera web conectata la calculator

9b Alternativ se poate folosi telefonul personal pentru a testa aplicatia, se conecteaza printr-un cablu la calculator, se activeaza "Developer Settings" iar mai apoi "Developer Settings" -> "USB debugging" (din sectiunea Debugging) si se permite. Se poate deasemenea se poate folosi si conectarea prin Wi-Fi: se asigura ca atat telefonul cat si calculatorul sunt conectate la aceeasi retea de internet, se activeaza "Wireless debugging" si "pair device with pairing code". Se asteapta in jur de 1 minut si se conecteaza automat, daca nu, se merge la Android Studio si "Available devices" -> "Pair Devices Using Wi-fi" -> "Pair using pairing code" si se introduce codul afisat pe telefon

10 Dupa pornirea aplicatiei, se realizeaza signup-ul urmat de login, se acorda permisiunile necesare si se utilizeaza

       
