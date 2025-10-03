package tests;


import base.BaseTest;
import org.testng.annotations.Test;
import pages.*;


public class InsiderQATest extends BaseTest {


    @Test(description = "01 - Home page opens and top nav is visible")
    public void case01_openHome() {
        new HomePage(driver)
                .open()
                .verifyHomePage();
    }


    @Test(description = "02 - Careers page opens and required blocks are visible")
    public void case02_goToCareers() {
        new HomePage(driver)
                .open()
                .verifyHomePage()
                .navigateToCareersViaMenu()
                .verifyCareerPage()
                .verifyBlocks();
    }


    @Test(description = "03 - QA jobs are listed after filtering by Istanbul & QA")
    public void case03_openFilteredQAJobs() {
        new QAJobsPage(driver)
                .openQAJobsLanding()
                .clickSeeAllQAJobs()
                .filterJobs("Istanbul, Turkiye", "Quality Assurance")
                .verifyJobsListPresent();
    }

    @Test(description = "04 - Tüm ilan içerikleri: Position/Department/Location doğrulaması")
    public void case04_verifyJobContents() {
        new QAJobsPage(driver)
                .openQAJobsLanding()
                .clickSeeAllQAJobs()
                .filterJobs("Istanbul, Turkiye", "Quality Assurance")
                .verifyJobsListPresent()
                .verifyEachJobContent("Software Quality Assurance Engineer", "Quality Assurance", "Istanbul, Turkiye");
    }
    @Test(description = "05 - View Role tıklandığında Lever başvuru sayfası açılmalı")
    public void case05_viewRoleRedirectsToLever() {
        new QAJobsPage(driver)
                .openQAJobsLanding()
                .clickSeeAllQAJobs()
                .filterJobs("Istanbul, Turkiye", "Quality Assurance")
                .verifyJobsListPresent()
                .openFirstJob();

        new JobDetailPage(driver).verifyLeverPage("Istanbul, Turkiye", "Quality Assurance");
    }

}