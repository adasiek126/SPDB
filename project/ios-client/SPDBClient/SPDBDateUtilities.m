#import "SPDBDateUtilities.h"

@implementation SPDBDateUtilities

- (NSDate *)stripSecondsFromDate:(NSDate *)date
{
    NSDateComponents *secondsOfDate = [[NSCalendar currentCalendar] components:NSSecondCalendarUnit
                                                                      fromDate:date];
    [secondsOfDate setSecond:-[secondsOfDate second]];
    
    NSDate *strippedDate = [[NSCalendar currentCalendar] dateByAddingComponents:secondsOfDate toDate:date options:0];
    
    return strippedDate;
}

- (NSDate *)subtractSeconds:(long)seconds fromDate:(NSDate *)date
{
    NSDateComponents *secondsAsDateComponent = [NSDateComponents new];
    [secondsAsDateComponent setSecond:-seconds];
    
    NSDate *calculatedDate = [[NSCalendar currentCalendar] dateByAddingComponents:secondsAsDateComponent toDate:date options:0];
    
    return calculatedDate;
}

- (NSString *)formatDate:(NSDate *)date withFormat:(NSString *)format
{
    NSDateFormatter *formatter = [NSDateFormatter new];
    [formatter setDateFormat:format];
    
    return [formatter stringFromDate:date];
}

@end
