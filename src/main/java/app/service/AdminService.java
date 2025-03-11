package app.service;

import app.exceptions.DatabaseException;
import app.model.Admin;
import app.persistence.AdminRepo;

public class AdminService {

    private final AdminRepo adminRepo;

    // Constructor injection of AdminRepo
    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public boolean validateAdmin(String username, String password) {
        return adminRepo.validateAdmin(username, password);
    }

    public void addAdmin(Admin admin) throws DatabaseException {
        adminRepo.addAdmin(admin);
    }

    public boolean deleteAdmin(int id) throws DatabaseException {
        return adminRepo.deleteAdmin(id);
    }
}
