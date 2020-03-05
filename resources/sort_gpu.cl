void runSingleSort(int index, global int* indices, global float* values);

kernel void
sortAKernel(global int* indices, global float* values)
{
  int index = get_global_id(0) * 2;
  runSingleSort(index, indices, values);
}

kernel void
sortBKernel(global int* indices, global float* values)
{
  int index = (get_global_id(0) * 2) + 1;
  runSingleSort(index, indices, values);
}

inline void runSingleSort(int index, global int* indices, global float* values)
{
  if (values[index] > values[index + 1])
  {
    // swap
    float temp = values[index];
    values[index] = values[index + 1];
    values[index + 1] = temp;

    int tempIndex = indices[index];
    indices[index] = indices[index + 1];
    indices[index + 1] = tempIndex;
  }
}
