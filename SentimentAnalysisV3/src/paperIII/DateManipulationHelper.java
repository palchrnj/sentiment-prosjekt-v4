package paperIII;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class DateManipulationHelper {
	
	
	public DateManipulationHelper(){
		
	}
	
	
	public ArrayList<DateTime> getListExtraordinaryOfClosedStockDays(){
		ArrayList<DateTime> extraordinaryClosedStockDays = new ArrayList<>();
		
		//2007
		DateTime firstNewYearsDay2007 = new DateTime(2007, 1, 1, 0, 0);
		DateTime wednesdayBeforeEaster2007 = new DateTime(2007, 4, 4, 0, 0);
		DateTime cutThursday2007 = new DateTime(2007, 4, 5, 0, 0);
		DateTime longFriday2007 = new DateTime(2007, 4, 6, 0, 0);
		DateTime firstEasterDay2007 = new DateTime(2007, 4, 9, 0, 0);
		DateTime firstOfMay2007 = new DateTime(2007, 5, 1, 0, 0);
		DateTime nationalDay2007 = new DateTime(2007, 5, 17, 0, 0);
		DateTime secondPinchDay2007 = new DateTime(2007, 5, 28, 0, 0);
		DateTime christmasEve2007 = new DateTime(2007, 12, 24, 0, 0);
		DateTime christmasDay2007 = new DateTime(2007, 12, 25, 0, 0);
		DateTime secondchristmasDay2007 = new DateTime(2007, 12, 26, 0, 0);
		DateTime newYearEve2007 = new DateTime(2007, 12, 31, 0, 0);
		//Add to list
		extraordinaryClosedStockDays.add(firstNewYearsDay2007);
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2007);
		extraordinaryClosedStockDays.add(cutThursday2007);
		extraordinaryClosedStockDays.add(longFriday2007);
		extraordinaryClosedStockDays.add(firstEasterDay2007);
		extraordinaryClosedStockDays.add(firstOfMay2007);
		extraordinaryClosedStockDays.add(nationalDay2007);
		extraordinaryClosedStockDays.add(secondPinchDay2007);
		extraordinaryClosedStockDays.add(christmasEve2007);
		extraordinaryClosedStockDays.add(christmasDay2007);
		extraordinaryClosedStockDays.add(secondchristmasDay2007);
		extraordinaryClosedStockDays.add(newYearEve2007);
		
		//2008
		DateTime firstNewYearsDay2008 = new DateTime(2008, 1, 1, 0, 0);
		DateTime wednesdayBeforeEaster2008 = new DateTime(2008, 3, 21, 0, 0);
		DateTime cutThursday2008 = new DateTime(2008, 3, 20, 0, 0);
		DateTime longFriday2008 = new DateTime(2008, 3, 21, 0, 0);
		DateTime secondEasterDay2008 = new DateTime(2008, 3, 24, 0, 0);
		DateTime firstOfMay2008 = new DateTime(2008, 5, 1, 0, 0);
		DateTime secondPinchDay2008 = new DateTime(2008, 5, 12, 0, 0);
		DateTime nationalDay2008 = new DateTime(2008, 5, 17, 0, 0);
		DateTime christmasEve2008 = new DateTime(2008, 12, 24, 0, 0);
		DateTime christmasDay2008 = new DateTime(2008, 12, 25, 0, 0);
		DateTime secondchristmasDay2008 = new DateTime(2008, 12, 26, 0, 0);
		DateTime newYearEve2008 = new DateTime(2008, 12, 31, 0, 0);
		//Add to list
		extraordinaryClosedStockDays.add(firstNewYearsDay2008);
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2008);
		extraordinaryClosedStockDays.add(cutThursday2008);
		extraordinaryClosedStockDays.add(longFriday2008);
		extraordinaryClosedStockDays.add(secondEasterDay2008);
		extraordinaryClosedStockDays.add(firstOfMay2008);
		extraordinaryClosedStockDays.add(nationalDay2008);
		extraordinaryClosedStockDays.add(secondPinchDay2008);
		extraordinaryClosedStockDays.add(christmasEve2008);
		extraordinaryClosedStockDays.add(christmasDay2008);
		extraordinaryClosedStockDays.add(secondchristmasDay2008);
		extraordinaryClosedStockDays.add(newYearEve2008);
		
		//2009
		DateTime firstNewYearsDay2009 = new DateTime(2009, 1, 1, 0, 0);
		DateTime wednesdayBeforeEaster2009 = new DateTime(2009, 4, 8, 0, 0);
		DateTime cutThursday2009 = new DateTime(2009, 4, 9, 0, 0);
		DateTime longFriday2009 = new DateTime(2009, 4, 10, 0, 0);
		DateTime secondEasterDay2009 = new DateTime(2009, 4, 13, 0, 0);
		DateTime firstOfMay2009 = new DateTime(2009, 5, 1, 0, 0);
		DateTime christSkyTravelDay2009 = new DateTime(2009, 5, 21, 0, 0);
		DateTime secondPinchDay2009 = new DateTime(2009, 6, 1, 0, 0);
		DateTime christmasEve2009 = new DateTime(2009, 12, 24, 0, 0);
		DateTime christmasDay2009 = new DateTime(2009, 12, 25, 0, 0);
		DateTime newYearEve2009 = new DateTime(2009, 12, 31, 0, 0);
		//Add to list
		extraordinaryClosedStockDays.add(firstNewYearsDay2009);
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2009);
		extraordinaryClosedStockDays.add(cutThursday2009);
		extraordinaryClosedStockDays.add(longFriday2009);
		extraordinaryClosedStockDays.add(secondEasterDay2009);
		extraordinaryClosedStockDays.add(firstOfMay2009);
		extraordinaryClosedStockDays.add(christSkyTravelDay2009);
		extraordinaryClosedStockDays.add(secondPinchDay2009);
		extraordinaryClosedStockDays.add(christmasEve2009);
		extraordinaryClosedStockDays.add(christmasDay2009);
		extraordinaryClosedStockDays.add(newYearEve2009);
		
		//2010
		DateTime firstNewYearsDay2010 = new DateTime(2010, 1, 1, 0, 0);
		DateTime wednesdayBeforeEaster2010 = new DateTime(2010, 3, 31, 0, 0);
		DateTime cutThursday2010 = new DateTime(2010, 4, 1, 0, 0);
		DateTime longFriday2010 = new DateTime(2010, 4, 2, 0, 0);
		DateTime secondEasterDay2010 = new DateTime(2010, 4, 5, 0, 0);
		DateTime christSkyTravelDay2010 = new DateTime(2010, 5, 13, 0, 0);
		DateTime nationalDay2010 = new DateTime(2010, 5, 17, 0, 0);
		DateTime secondPinchDay2010 = new DateTime(2010, 5, 24, 0, 0);
		DateTime christmasEve2010 = new DateTime(2010, 12, 24, 0, 0);
		DateTime newYearEve2010 = new DateTime(2010, 12, 31, 0, 0);
		//Add to list
		extraordinaryClosedStockDays.add(firstNewYearsDay2010);
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2010);
		extraordinaryClosedStockDays.add(cutThursday2010);
		extraordinaryClosedStockDays.add(longFriday2010);
		extraordinaryClosedStockDays.add(secondEasterDay2010);
		extraordinaryClosedStockDays.add(christSkyTravelDay2010);
		extraordinaryClosedStockDays.add(nationalDay2010);
		extraordinaryClosedStockDays.add(secondPinchDay2010);
		extraordinaryClosedStockDays.add(christmasEve2010);
		extraordinaryClosedStockDays.add(newYearEve2010);
		
		//2011
		DateTime wednesdayBeforeEaster2011 = new DateTime(2011, 4, 20, 0, 0);
		DateTime cutThursday2011 = new DateTime(2011, 4, 21, 0, 0);
		DateTime longFriday2011 = new DateTime(2011, 4, 22, 0, 0);
		DateTime secondEasterDay2011 = new DateTime(2011, 4, 25, 0, 0);
		DateTime nationalDay2011 = new DateTime(2011, 5, 17, 0, 0);
		DateTime christSkyTravelDay2011 = new DateTime(2011, 6, 2, 0, 0);
		DateTime secondPinchDay2011 = new DateTime(2011, 6, 13, 0, 0);
		DateTime secondchristmasDay2011 = new DateTime(2011, 12, 26, 0, 0);
		//Add to list
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2011);
		extraordinaryClosedStockDays.add(cutThursday2011);
		extraordinaryClosedStockDays.add(longFriday2011);
		extraordinaryClosedStockDays.add(secondEasterDay2011);
		extraordinaryClosedStockDays.add(christSkyTravelDay2011);
		extraordinaryClosedStockDays.add(nationalDay2011);
		extraordinaryClosedStockDays.add(secondPinchDay2011);
		extraordinaryClosedStockDays.add(secondchristmasDay2011);
		
		//2012
		DateTime wednesdayBeforeEaster2012 = new DateTime(2012, 4, 4, 0, 0);
		DateTime cutThursday2012 = new DateTime(2012, 4, 5, 0, 0);
		DateTime longFriday2012 = new DateTime(2012, 4, 6, 0, 0);
		DateTime secondEasterDay2012 = new DateTime(2012, 4, 9, 0, 0);
		DateTime firstOfMay2012 = new DateTime(2012, 5, 1, 0, 0);
		DateTime nationalDay2012 = new DateTime(2012, 5, 17, 0, 0);
		DateTime secondPinchDay2012 = new DateTime(2012, 5, 28, 0, 0);
		DateTime christmasEve2012 = new DateTime(2012, 12, 24, 0, 0);
		DateTime christmasDay2012 = new DateTime(2012, 12, 25, 0, 0);
		DateTime secondchristmasDay2012 = new DateTime(2012, 12, 26, 0, 0);
		DateTime newYearEve2012 = new DateTime(2012, 12, 31, 0, 0);
		//Add to list
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2012);
		extraordinaryClosedStockDays.add(cutThursday2012);
		extraordinaryClosedStockDays.add(longFriday2012);
		extraordinaryClosedStockDays.add(secondEasterDay2012);
		extraordinaryClosedStockDays.add(firstOfMay2012);
		extraordinaryClosedStockDays.add(nationalDay2012);
		extraordinaryClosedStockDays.add(secondPinchDay2012);
		extraordinaryClosedStockDays.add(christmasEve2012);
		extraordinaryClosedStockDays.add(christmasDay2012);
		extraordinaryClosedStockDays.add(secondchristmasDay2012);
		extraordinaryClosedStockDays.add(newYearEve2012);
		
		//2013
		DateTime firstNewYearsDay2013 = new DateTime(2013, 1, 1, 0, 0);
		DateTime wednesdayBeforeEaster2013 = new DateTime(2013, 3, 27, 0, 0);
		DateTime cutThursday2013 = new DateTime(2013, 3, 28, 0, 0);
		DateTime longFriday2013 = new DateTime(2013, 3, 29, 0, 0);
		DateTime secondEasterDay2013 = new DateTime(2013, 4, 1, 0, 0);
		DateTime christSkyTravelDay2013 = new DateTime(2013, 5, 9, 0, 0);
		DateTime firstOfMay2013 = new DateTime(2013, 5, 1, 0, 0);
		DateTime nationalDay2013 = new DateTime(2013, 5, 17, 0, 0);
		DateTime secondPinchDay2013 = new DateTime(2013, 5, 20, 0, 0);
		DateTime christmasEve2013 = new DateTime(2013, 12, 24, 0, 0);
		DateTime christmasDay2013 = new DateTime(2013, 12, 25, 0, 0);
		DateTime secondchristmasDay2013 = new DateTime(2013, 12, 26, 0, 0);
		DateTime newYearEve2013 = new DateTime(2013, 12, 31, 0, 0);
		//Add to list
		extraordinaryClosedStockDays.add(firstNewYearsDay2013);
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2013);
		extraordinaryClosedStockDays.add(cutThursday2013);
		extraordinaryClosedStockDays.add(longFriday2013);
		extraordinaryClosedStockDays.add(secondEasterDay2013);
		extraordinaryClosedStockDays.add(christSkyTravelDay2013);
		extraordinaryClosedStockDays.add(firstOfMay2013);
		extraordinaryClosedStockDays.add(nationalDay2013);
		extraordinaryClosedStockDays.add(secondPinchDay2013);
		extraordinaryClosedStockDays.add(christmasEve2013);
		extraordinaryClosedStockDays.add(christmasDay2013);
		extraordinaryClosedStockDays.add(secondchristmasDay2013);
		extraordinaryClosedStockDays.add(newYearEve2013);
		
		//2014
		DateTime firstNewYearsDay2014 = new DateTime(2014, 1, 1, 0, 0);
		DateTime wednesdayBeforeEaster2014 = new DateTime(2014, 4, 16, 0, 0);
		DateTime cutThursday2014 = new DateTime(2014, 4, 17, 0, 0);
		DateTime longFriday2014 = new DateTime(2014, 4, 18, 0, 0);
		DateTime secondEasterDay2014 = new DateTime(2014, 4, 21, 0, 0);
		DateTime christSkyTravelDay2014 = new DateTime(2014, 5, 29, 0, 0);
		DateTime firstOfMay2014 = new DateTime(2014, 5, 1, 0, 0);
		
		//Add to list
		extraordinaryClosedStockDays.add(firstNewYearsDay2014);
		extraordinaryClosedStockDays.add(wednesdayBeforeEaster2014);
		extraordinaryClosedStockDays.add(cutThursday2014);
		extraordinaryClosedStockDays.add(longFriday2014);
		extraordinaryClosedStockDays.add(secondEasterDay2014);
		extraordinaryClosedStockDays.add(christSkyTravelDay2014);
		extraordinaryClosedStockDays.add(firstOfMay2014);

		
		return extraordinaryClosedStockDays;
	}

}
