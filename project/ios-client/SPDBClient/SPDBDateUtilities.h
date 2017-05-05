#import <Foundation/Foundation.h>

@interface SPDBDateUtilities : NSObject

- (NSDate *)stripSecondsFromDate:(NSDate *)date;
- (NSDate *)subtractSeconds:(long)seconds fromDate:(NSDate *)date;
- (NSString *)formatDate:(NSDate *)date withFormat:(NSString *)format;

@end
