#import <Foundation/Foundation.h>

@protocol SPDBShortestPathFetcherDelegate <NSObject>

- (void)updateProgress:(CGFloat)progress;
- (void)didFetchShortestPath:(NSArray *)shortestPath;
- (void)didFailFetchingShortestPath:(NSError *)error;

@end
