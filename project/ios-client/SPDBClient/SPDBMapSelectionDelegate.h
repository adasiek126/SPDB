#import <Foundation/Foundation.h>

@protocol SPDBMapSelectionDelegate <NSObject>

- (void)didSelectPoint:(NSValue *)point atIndex:(NSUInteger)index;

@end
